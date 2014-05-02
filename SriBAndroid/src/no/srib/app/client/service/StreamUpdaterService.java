package no.srib.app.client.service;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.srib.app.client.asynctask.HttpAsyncTask;
import no.srib.app.client.asynctask.HttpAsyncTask.HttpResponseListener;
import no.srib.app.client.model.StreamSchedule;
import no.srib.app.client.service.StreamUpdaterService.OnStreamUpdateListener.Error;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class StreamUpdaterService extends Service {

	private static final int MAX_TIMER_FAILS = 2;
	private static final int TIMER_FAIL_TRESHOLD = 10000;

	private final IBinder binder;

	private AtomicInteger timerFails;
	private Handler timerHandler;
	private Runnable streamScheduleUpdater;
	private OnStreamUpdateListener streamUpdateListener;

	public StreamUpdaterService() {
		binder = new StreamUpdaterBinder();
	}

	public void setStreamUpdateListener(
			OnStreamUpdateListener streamUpdateListener) {
		this.streamUpdateListener = streamUpdateListener;
	}

	public void updateFrom(String updateURL) {
		if (streamScheduleUpdater == null) {
			streamScheduleUpdater = new StreamScheduleUpdater(updateURL);
			timerHandler.postDelayed(streamScheduleUpdater, 0);
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();

		timerFails = new AtomicInteger(0);
		timerHandler = new Handler();
		streamScheduleUpdater = null;
		streamUpdateListener = null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		timerHandler.removeCallbacks(streamScheduleUpdater);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	public class StreamUpdaterBinder extends Binder {
		public StreamUpdaterService getService() {
			return StreamUpdaterService.this;
		}
	}

	public interface OnStreamUpdateListener {
		enum Error {
			NO_INTERNET,
			SERVER_UNREACHABLE
		}

		void onError(Error error);

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

			Log.d("SriB", "Updating the stream schedule...");
		}
	}

	private class StreamScheduleResponseListener implements
			HttpResponseListener {

		@Override
		public void onResponse(String response) {
			if (response == null) {
				if (!isNetworkAvailable()) {
					streamUpdateListener.onError(Error.NO_INTERNET);
				} else {
					streamUpdateListener.onError(Error.SERVER_UNREACHABLE);
				}
			} else {
				Log.d("SriB", response);
				ObjectMapper mapper = new ObjectMapper();

				try {
					StreamSchedule streamSchedule = mapper.readValue(response,
							StreamSchedule.class);

					if (streamUpdateListener != null) {
						streamUpdateListener.onStreamUpdate(streamSchedule);
					}

					long delay = streamSchedule.getTime()
							- System.currentTimeMillis();

					Log.d("SriB", "delay: " + delay);

					if (delay < 0) {
						delay = 0;
					}

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
				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					Log.e("SriB", e.getMessage());
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					Log.e("SriB", e.getMessage());
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.e("SriB", e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
