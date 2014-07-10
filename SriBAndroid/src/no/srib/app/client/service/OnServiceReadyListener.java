package no.srib.app.client.service;

public interface OnServiceReadyListener<T extends BaseService> {

	void onServiceReady(T service);
}
