package no.srib.app.client.service;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import no.srib.app.client.asynctask.HttpAsyncTask;
import no.srib.app.client.asynctask.HttpAsyncTask.HttpResponseListener;
import no.srib.app.client.model.StreamSchedule;
import no.srib.app.client.receiver.ConnectivityChangeReceiver;
import no.srib.app.client.receiver.ConnectivityChangeReceiver.OnConnectionChangedListener;
import no.srib.app.client.service.StreamUpdaterService.OnStreamUpdateListener.Status;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

public class StreamUpdaterService extends BaseService {

	private static final int MAX_TIMER_FAILS = 2;
	private static final int TIMER_FAIL_TRESHOLD = 10000;

	private final ObjectMapper MAPPER;

	private AtomicBoolean updating;
	private AtomicInteger timerFails;
	private Handler timerHandler;
	private Runnable streamScheduleUpdater;
	private OnStreamUpdateListener streamUpdateListener;
	private ConnectivityChangeReceiver connectionChangeReceiver;

	public StreamUpdaterService() {
		MAPPER = new ObjectMapper();
	}

	@Override
	public void onCreate() {
		super.onCreate();

		updating = new AtomicBoolean(false);
		timerFails = new AtomicInteger(0);
		timerHandler = new Handler();
		streamScheduleUpdater = null;
		streamUpdateListener = null;
		connectionChangeReceiver = new ConnectivityChangeReceiver();
		connectionChangeReceiver
				.setConnectionChangedListener(new ConnectionChangedListener());

		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(connectionChangeReceiver, filter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		stopUpdating();

		unregisterReceiver(connectionChangeReceiver);
	}

	public void setStreamUpdateListener(
			OnStreamUpdateListener streamUpdateListener) {
		this.streamUpdateListener = streamUpdateListener;
	}

	public void updateFrom(String updateURL) {
		if (streamScheduleUpdater == null) {
			streamScheduleUpdater = new StreamScheduleUpdater(updateURL);
			timerHandler.postDelayed(streamScheduleUpdater, 0);
			updating.set(true);
		}
	}

	public boolean isUpdating() {
		return updating.get();
	}

	public void update() {
		if (streamScheduleUpdater != null) {
			timerHandler.postDelayed(streamScheduleUpdater, 0);
			updating.set(true);
		}
	}

	public void stopUpdating() {
		timerHandler.removeCallbacks(streamScheduleUpdater);
		updating.set(false);
	}

	public interface OnStreamUpdateListener {
		enum Status {
			NO_INTERNET,
			SERVER_UNREACHABLE,
			INVALID_RESPONSE,
			CONNECTING
		}

		void onStatus(Status status);

		void onStreamUpdate(StreamSchedule streamSchedule);
	}

	private class StreamScheduleUpdater implements Runnable {

		private String updateURL;

		public StreamScheduleUpdater(String updateURL) {
			this.updateURL = updateURL;
		}

		@Override
		public void run() {
			HttpAsyncTask streamScheduleTask = new HttpAsyncTask(
					new StreamScheduleResponseListener());
			streamScheduleTask.execute(updateURL);

			streamUpdateListener.onStatus(Status.CONNECTING);
		}
	}

	private class StreamScheduleResponseListener implements
			HttpResponseListener {

		@Override
		public void onResponse(String response) {
			updating.set(false);

			if (response == null) {
				if (connectionChangeReceiver
						.networkAvailable(getApplicationContext())) {
					streamUpdateListener.onStatus(Status.SERVER_UNREACHABLE);
				} else {
					streamUpdateListener.onStatus(Status.NO_INTERNET);
				}
			} else {
				Log.d("SriB", response);

				try {
					StreamSchedule streamSchedule = MAPPER.readValue(response,
							StreamSchedule.class);

					if (streamUpdateListener != null) {
						streamUpdateListener.onStreamUpdate(streamSchedule);
					}

					long delay = streamSchedule.getTime()
							- System.currentTimeMillis();

					Log.d("SriB", "delay: " + delay);

					if (delay < TIMER_FAIL_TRESHOLD) {
						timerFails.incrementAndGet();
						Log.d("SriB", "timerFails: " + timerFails.get());
					} else {
						timerFails.set(0);
					}

					if (timerFails.get() < MAX_TIMER_FAILS) {
						timerHandler.removeCallbacks(streamScheduleUpdater);
						timerHandler.postDelayed(streamScheduleUpdater, delay);
					} else {
						Log.d("SriB", "Time on client is set too far ahead");
					}
				} catch (IOException e) {
					streamUpdateListener.onStatus(Status.INVALID_RESPONSE);
				}
			}
		}
	}

	private class ConnectionChangedListener implements
			OnConnectionChangedListener {

		@Override
		public void onNetworkAvailable() {
			update();
		}

		@Override
		public void onNetworkUnavailable() {
			stopUpdating();

			if (streamUpdateListener != null) {
				streamUpdateListener.onStatus(Status.NO_INTERNET);
			}
		}
	}
}
