package no.srib.app.client.imageloader;

public enum UrlImageLoaderProvider {
	INSTANCE;

	private UrlImageLoader urlImageLoader;

	private UrlImageLoaderProvider() {
		urlImageLoader = new UrlImageLoaderIon();
	}

	public UrlImageLoader get() {
		return urlImageLoader;
	}
}
