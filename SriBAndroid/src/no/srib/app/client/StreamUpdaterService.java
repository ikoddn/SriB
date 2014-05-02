package no.srib.app.client;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.srib.app.client.asynctask.HttpAsyncTask;
import no.srib.app.client.asynctask.HttpAsyncTask.HttpResponseListener;
import no.srib.app.client.model.StreamSchedule;

import android.app.Service;
import android.content.Intent;
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

	@Override
	public void onCreate() {
		super.onCreate();

		timerFails = new AtomicInteger(0);
		timerHandler = new Handler();

		streamScheduleUpdater = new StreamScheduleUpdater();

		timerHandler.postDelayed(streamScheduleUpdater, 0);
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
		void onStreamUpdate(StreamSchedule streamSchedule);
	}

	private class StreamScheduleUpdater implements Runnable {

		@Override
		public void run() {
			HttpAsyncTask streamScheduleTask = new HttpAsyncTask(
					new StreamScheduleResponseListener());
			streamScheduleTask
					.execute("http://80.203.58.154:8080/SriBServer/rest/radiourl");

			Log.d("SriB", "Updating the stream schedule...");
		}
	}

	private class StreamScheduleResponseListener implements
			HttpResponseListener {

		@Override
		public void onResponse(String response) {
			if (response != null) {
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
			} else {
				Log.d("SriB", "result == null");
			}
		}
	}
}
