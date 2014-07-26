package no.srib.app.client.dao.retrofit;

import no.srib.app.client.dao.PodcastProgramsDAO;
import no.srib.app.client.dao.exception.DAOException;
import no.srib.app.client.model.PodcastPrograms;

import org.apache.http.HttpStatus;

import retrofit.RetrofitError;
import retrofit.client.Response;

import com.fasterxml.jackson.databind.JavaType;

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
			result = convertToType(response, type);
		}

		return result;
	}
}
