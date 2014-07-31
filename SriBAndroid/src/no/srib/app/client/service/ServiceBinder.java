package no.srib.app.client.service;

import no.srib.app.client.event.handler.ServiceConnectionHandler;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

public class ServiceBinder<T extends BaseService> {

	private final Class<T> typeClass;
	private final ServiceConnection connection;

	private boolean serviceBound;

	public ServiceBinder(final Class<T> typeClass) {
		this.typeClass = typeClass;
		connection = new ServiceConnectionHandler<T>(typeClass);

		serviceBound = false;
	}

	public void bind(final Context context) {
		Context appContext = context.getApplicationContext();

		appContext.bindService(new Intent(context, typeClass), connection,
				Context.BIND_AUTO_CREATE);
		serviceBound = true;
	}

	public void unbind(final Context context) {
		Context appContext = context.getApplicationContext();

		if (serviceBound) {
			appContext.unbindService(connection);
			serviceBound = false;
		}
	}
}
