package no.srib.app.client.dao.retrofit;

import no.srib.app.client.dao.ScheduleDAO;
import no.srib.app.client.dao.exception.DAOException;
import no.srib.app.client.model.Schedule;

import org.apache.http.HttpStatus;

import retrofit.RetrofitError;
import retrofit.client.Response;

import com.fasterxml.jackson.databind.JavaType;

public class ScheduleDAOImpl extends BaseDAOImpl implements ScheduleDAO {

	private final JavaType type;

	public ScheduleDAOImpl(final String restApiUrl) {
		super(restApiUrl);

		type = mapper.getTypeFactory().constructType(Schedule.class);
	}

	@Override
	public Schedule get() throws DAOException {
		Schedule result = null;
		Response response = null;

		try {
			response = appServer.getCurrentSchedule();
		} catch (RetrofitError e) {
			throw new DAOException(e);
		}

		if (response != null && response.getStatus() == HttpStatus.SC_OK) {
			result = convertToType(response, type);
		}

		return result;
	}
}
