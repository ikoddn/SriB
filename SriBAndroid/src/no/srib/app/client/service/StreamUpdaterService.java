package no.srib.app.client.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import no.srib.app.client.asynctask.HttpAsyncTask;
import no.srib.app.client.asynctask.HttpAsyncTask.HttpResponseListener;
import no.srib.app.client.model.StreamSchedule;
import no.srib.app.client.service.StreamUpdaterService.OnStreamUpdateListener.Status;
import android.os.Handler;

import com.fasterxml.jackson.databind.ObjectMapper;

public class StreamUpdaterService extends BaseService {

	private static final int MAX_TIMER_FAILS = 2;

	private final ObjectMapper MAPPER;

	private AtomicBoolean currentlyUpdating;
	private AtomicBoolean updateScheduled;
	private AtomicInteger timerFails;
	private Handler timerHandler;
	private Runnable streamScheduleUpdater;
	private OnStreamUpdateListener streamUpdateListener;

	public StreamUpdaterService() {
		MAPPER = new ObjectMapper();
	}

	@Override
	public void onCreate() {
		super.onCreate();

		currentlyUpdating = new AtomicBoolean(false);
		updateScheduled = new AtomicBoolean(false);
		timerFails = new AtomicInteger(0);
		timerHandler = new Handler();
		streamScheduleUpdater = null;
		streamUpdateListener = null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		stopUpdating();
	}

	public void setStreamUpdateListener(
			OnStreamUpdateListener streamUpdateListener) {
		this.streamUpdateListener = streamUpdateListener;
	}

	public void setUpdateURL(final String updateURL) {
		streamScheduleUpdater = new StreamScheduleUpdater(updateURL);
	}

	public boolean hasUpdateScheduled() {
		return updateScheduled.get();
	}

	public void update() {
		if (!currentlyUpdating.get()) {
			updateIn(0);
		}
	}

	public void updateAt(final long time) {
		long currentTime = Calendar.getInstance().getTimeInMillis();
		updateIn(time - currentTime);
	}

	private void updateIn(final long delayParam) {
		long delay = delayParam;

		if (delay < 0) {
			timerFails.incrementAndGet();
			delay = 0;
		} else {
			timerFails.set(0);
		}

		if (timerFails.get() < MAX_TIMER_FAILS) {
			updateScheduled.set(true);
			timerHandler.removeCallbacks(streamScheduleUpdater);
			timerHandler.postDelayed(streamScheduleUpdater, delay);
		} else {
			stopUpdating();
		}
	}

	public void stopUpdating() {
		timerHandler.removeCallbacks(streamScheduleUpdater);
		currentlyUpdating.set(false);
		updateScheduled.set(false);
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
			if (!currentlyUpdating.get()) {
				currentlyUpdating.set(true);
				HttpAsyncTask streamScheduleTask = new HttpAsyncTask(
						new StreamScheduleResponseListener());
				streamScheduleTask.execute(updateURL);

				streamUpdateListener.onStatus(Status.CONNECTING);
			}
		}
	}

	private class StreamScheduleResponseListener implements
			HttpResponseListener {

		@Override
		public void onResponse(int statusCode, String response) {
			if (response == null) {
				updateScheduled.set(false);
				streamUpdateListener.onStatus(Status.SERVER_UNREACHABLE);
			} else {
				try {
					StreamSchedule streamSchedule = MAPPER.readValue(response,
							StreamSchedule.class);

					streamUpdateListener.onStreamUpdate(streamSchedule);

					updateAt(streamSchedule.getTime() * 1000);
				} catch (IOException e) {
					streamUpdateListener.onStatus(Status.INVALID_RESPONSE);
				}
			}

			currentlyUpdating.set(false);
		}
	}
}
