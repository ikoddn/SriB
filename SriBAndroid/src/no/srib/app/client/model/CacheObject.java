package no.srib.app.client.model;

import java.io.Serializable;

public class CacheObject<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private long expirationTime;
	private T data;

	public CacheObject() {
	}

	public CacheObject(final T data, final long expirationTime) {
		this.expirationTime = expirationTime;
		this.data = data;
	}

	public long getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(final long expirationTime) {
		this.expirationTime = expirationTime;
	}

	public T getData() {
		return data;
	}

	public void setData(final T data) {
		this.data = data;
	}
}
