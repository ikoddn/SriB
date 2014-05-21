package no.srib.app.client.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public abstract class BaseService extends Service {

	private final IBinder BINDER;

	public BaseService() {
		BINDER = new BaseBinder();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return BINDER;
	}

	public class BaseBinder extends Binder {
		public BaseService getService() {
			return BaseService.this;
		}
	}
}
