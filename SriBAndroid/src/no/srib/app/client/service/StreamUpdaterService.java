package no.srib.app.client.service;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import no.srib.app.client.asynctask.StreamScheduleAsyncTask;
import no.srib.app.client.model.StreamSchedule;
import no.srib.app.client.service.StreamUpdaterService.OnStreamUpdateListener.Status;
import android.content.Context;
import android.os.Handler;

public class StreamUpdaterService extends BaseService {

	private static final int MAX_TIMER_FAILS = 2;

	private AtomicBoolean currentlyUpdating;
	private AtomicBoolean updateScheduled;
	private AtomicInteger timerFails;
	private Handler timerHandler;
	private Runnable streamScheduleUpdater;
	private OnStreamUpdateListener streamUpdateListener;

	@Override
	public void onCreate() {
		super.onCreate();

		currentlyUpdating = new AtomicBoolean(false);
		updateScheduled = new AtomicBoolean(false);
		timerFails = new AtomicInteger(0);
		timerHandler = new Handler();
		streamScheduleUpdater = new StreamScheduleUpdater();
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

	public boolean hasUpdateScheduled() {
		return updateScheduled.get();
	}

	public void update() {
		if (!currentlyUpdating.get()) {
			updateIn(0);
		}
	}

	public void updateAt(final long time) {
		updateIn(time - System.currentTimeMillis());
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

		@Override
		public void run() {
			if (!currentlyUpdating.get()) {
				currentlyUpdating.set(true);

				final Context context = getApplicationContext();
				new StreamScheduleAsyncTask(context, StreamUpdaterService.this)
						.execute();

				streamUpdateListener.onStatus(Status.CONNECTING);
			}
		}
	}

	public void onNetworkAvailable() {
		update();
	}

	public void onNetworkUnavailable() {
		stopUpdating();
		streamUpdateListener.onStatus(Status.NO_INTERNET);
	}

	public void onUpdateFailed() {
		stopUpdating();
		streamUpdateListener.onStatus(Status.SERVER_UNREACHABLE);
		// TODO or invalid response, or whatever
	}

	public void onUpdateSuccess(final StreamSchedule streamSchedule) {
		streamUpdateListener.onStreamUpdate(streamSchedule);
		updateAt(streamSchedule.getTime() * 1000);

		currentlyUpdating.set(false);
	}
}
