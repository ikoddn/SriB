package no.srib.app.client.event.handler;

import no.srib.app.client.service.BaseService;
import no.srib.app.client.service.BaseService.BaseBinder;
import no.srib.app.client.util.BusProvider;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class ServiceConnectionHandler<T extends BaseService> implements
		ServiceConnection {

	private final Class<T> typeClass;

	public ServiceConnectionHandler(final Class<T> typeClass) {
		this.typeClass = typeClass;
	}

	@Override
	public void onServiceConnected(final ComponentName name,
			final IBinder binder) {

		BaseBinder customBinder = (BaseService.BaseBinder) binder;
		T service = typeClass.cast(customBinder.getService());

		BusProvider.INSTANCE.get().post(service);
	}

	@Override
	public void onServiceDisconnected(final ComponentName name) {
	}
}
