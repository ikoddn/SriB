package no.srib.app.client.dao.sharedpreferences;

import java.io.IOException;

import no.srib.app.client.dao.exception.DAOException;
import no.srib.app.client.model.CacheObject;
import android.content.Context;
import android.content.SharedPreferences;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseDAOImpl<T> {

	private static final String PREFS_NAME = "cache_prefs";

	protected final ObjectMapper mapper;
	private final SharedPreferences prefs;

	protected BaseDAOImpl(final Context context) {
		mapper = new ObjectMapper();
		prefs = context.getSharedPreferences(PREFS_NAME, 0);
	}

	protected CacheObject<T> load(final String key, final JavaType type)
			throws DAOException {
		CacheObject<T> result = null;

		String json = prefs.getString(key, null);

		if (json != null) {
			try {
				result = mapper.readValue(json, type);
			} catch (IOException e) {
				throw new DAOException(e);
			}
		}

		return result;
	}

	protected void save(final String key, final CacheObject<T> cacheObject)
			throws DAOException {
		try {
			String json = mapper.writeValueAsString(cacheObject);

			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(key, json);
			editor.commit();
		} catch (JsonProcessingException e) {
			throw new DAOException(e);
		}
	}
}
