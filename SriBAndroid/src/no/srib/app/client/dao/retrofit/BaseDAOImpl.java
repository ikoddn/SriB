package no.srib.app.client.dao.retrofit;

import java.io.IOException;

import no.srib.app.client.dao.exception.DAOException;
import retrofit.RestAdapter;
import retrofit.client.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
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

	protected <T> T convertToType(final Response response, final JavaType type)
			throws DAOException {

		T result = null;

		try {
			result = mapper.readValue(response.getBody().in(), type);
		} catch (JsonParseException e) {
			throw new DAOException(e);
		} catch (JsonMappingException e) {
			throw new DAOException(e);
		} catch (IOException e) {
			throw new DAOException(e);
		}

		return result;
	}
}
