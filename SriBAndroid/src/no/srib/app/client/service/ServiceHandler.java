package no.srib.app.client.service;

import no.srib.app.client.service.BaseService.BaseBinder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class ServiceHandler<T extends BaseService> {

	private final Class<T> TYPECLASS;

	private boolean serviceBound;
	private T service;
	private ServiceConnection connection;
	private OnServiceReadyListener onServiceReadyListener;

	public ServiceHandler(final Class<T> typeClass,
			OnServiceReadyListener listener) {

		TYPECLASS = typeClass;

		serviceBound = false;
		service = null;
		connection = new ServiceConnectionImpl();
		onServiceReadyListener = listener;
	}

	public T getService() {
		return service;
	}

	public void bind(Context context) {
		Context appContext = context.getApplicationContext();

		appContext.bindService(new Intent(context, TYPECLASS), connection,
				Context.BIND_AUTO_CREATE);
		serviceBound = true;
	}

	public void unbind(Context context) {
		Context appContext = context.getApplicationContext();

		if (serviceBound) {
			appContext.unbindService(connection);
			serviceBound = false;
		}
	}

	private class ServiceConnectionImpl implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {
			BaseBinder customBinder = (BaseService.BaseBinder) binder;
			service = TYPECLASS.cast(customBinder.getService());

			onServiceReadyListener.onServiceReady(service);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			service = null;
		}
	}

	public interface OnServiceReadyListener {
		void onServiceReady(BaseService service);
	}
}
