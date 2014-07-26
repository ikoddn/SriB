package no.srib.app.client.dao.retrofit;

import retrofit.RestAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseDAOImpl {

	protected final ObjectMapper mapper;

	protected AppServerService appServer;

	protected BaseDAOImpl(final String restApiUrl) {
		mapper = new ObjectMapper();

		RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(
				restApiUrl).build();

		appServer = restAdapter.create(AppServerService.class);
	}
}
