package no.srib.app.client.dao.retrofit;

import java.util.List;

import no.srib.app.client.dao.PodcastDAO;
import no.srib.app.client.dao.exception.DAOException;
import no.srib.app.client.model.Podcast;

import org.apache.http.HttpStatus;

import retrofit.RetrofitError;
import retrofit.client.Response;

import com.fasterxml.jackson.databind.JavaType;

public class PodcastDAOImpl extends BaseDAOImpl implements PodcastDAO {

	private final JavaType type;

	public PodcastDAOImpl(final String restApiUrl) {
		super(restApiUrl);

		type = mapper.getTypeFactory().constructCollectionType(List.class,
				Podcast.class);
	}

	@Override
	public List<Podcast> getList() throws DAOException {
		List<Podcast> list = null;
		Response response = null;

		try {
			response = appServer.getPodcasts();
		} catch (RetrofitError e) {
			throw new DAOException(e);
		}

		if (response != null && response.getStatus() == HttpStatus.SC_OK) {
			list = convertToType(response, type);
		}

		return list;
	}

	@Override
	public List<Podcast> getListFromProgram(final int programId)
			throws DAOException {

		List<Podcast> list = null;
		Response response = null;

		try {
			response = appServer.getPodcastsFromProgram(programId);
		} catch (RetrofitError e) {
			throw new DAOException(e);
		}

		if (response != null && response.getStatus() == HttpStatus.SC_OK) {
			list = convertToType(response, type);
		}

		return list;
	}
}
