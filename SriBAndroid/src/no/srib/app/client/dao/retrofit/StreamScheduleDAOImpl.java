package no.srib.app.client.dao.retrofit;

import no.srib.app.client.dao.StreamScheduleDAO;
import no.srib.app.client.dao.exception.DAOException;
import no.srib.app.client.model.StreamSchedule;

import org.apache.http.HttpStatus;

import retrofit.RetrofitError;
import retrofit.client.Response;

import com.fasterxml.jackson.databind.JavaType;

public class StreamScheduleDAOImpl extends BaseDAOImpl implements
		StreamScheduleDAO {

	private final JavaType type;

	public StreamScheduleDAOImpl(final String restApiUrl) {
		super(restApiUrl);

		type = mapper.getTypeFactory().constructType(StreamSchedule.class);
	}

	@Override
	public StreamSchedule get() throws DAOException {
		StreamSchedule result = null;
		Response response = null;

		try {
			response = appServer.getStreamSchedule();
		} catch (RetrofitError e) {
			throw new DAOException(e);
		}

		if (response != null && response.getStatus() == HttpStatus.SC_OK) {
			result = convertToType(response, type);
		}

		return result;
	}
}
