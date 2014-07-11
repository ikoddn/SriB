package no.srib.app.client.dao.sharedpreferences;

import java.io.IOException;

import no.srib.app.client.dao.StreamScheduleDAO;
import no.srib.app.client.dao.exception.DAOException;
import no.srib.app.client.model.StreamSchedule;
import android.content.Context;
import android.content.SharedPreferences;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StreamScheduleDAOImpl implements StreamScheduleDAO {

	private static final String PREFS_NAME = "prefs_streamschedule";
	private static final String KEY_STREAM = "key_stream";

	private final ObjectMapper MAPPER;

	private Context context;

	public StreamScheduleDAOImpl(final Context context) {
		MAPPER = new ObjectMapper();
		this.context = context;
	}

	@Override
	public StreamSchedule get() {
		StreamSchedule streamSchedule;

		SharedPreferences sharedPrefs = context.getSharedPreferences(
				PREFS_NAME, 0);
		String json = sharedPrefs.getString(KEY_STREAM, null);

		if (json != null) {
			try {
				streamSchedule = MAPPER.readValue(json, StreamSchedule.class);
			} catch (IOException e) {
				streamSchedule = null;
			}
		} else {
			streamSchedule = null;
		}

		return streamSchedule;
	}

	@Override
	public void set(final StreamSchedule streamSchedule) throws DAOException {
		try {
			String json = MAPPER.writeValueAsString(streamSchedule);

			SharedPreferences settings = context.getSharedPreferences(
					PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString(KEY_STREAM, json);
			editor.commit();
		} catch (JsonProcessingException e) {
			throw new DAOException(e);
		}
	}
}
