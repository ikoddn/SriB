package no.srib.app.client.dao.retrofit;

import java.io.IOException;

import org.apache.http.HttpStatus;

import retrofit.RetrofitError;
import retrofit.client.Response;
import no.srib.app.client.dao.PodcastProgramsDAO;
import no.srib.app.client.dao.exception.DAOException;
import no.srib.app.client.model.PodcastPrograms;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class PodcastProgramsDAOImpl extends BaseDAOImpl implements
		PodcastProgramsDAO {

	private final JavaType type;

	public PodcastProgramsDAOImpl(final String restApiUrl) {
		super(restApiUrl);

		type = mapper.getTypeFactory().constructType(PodcastPrograms.class);
	}

	@Override
	public PodcastPrograms get() throws DAOException {
		PodcastPrograms result = null;
		Response response = null;
		
		try {
			response = appServer.getPodcastPrograms();
		} catch (RetrofitError e) {
			throw new DAOException(e);
		}
		
		if (response != null && response.getStatus() == HttpStatus.SC_OK) {
			try {
				result = mapper.readValue(response.getBody().in(), type);
			} catch (JsonParseException e) {
				throw new DAOException(e);
			} catch (JsonMappingException e) {
				throw new DAOException(e);
			} catch (IOException e) {
				throw new DAOException(e);
			}
		}
		
		return result;
	}
}
