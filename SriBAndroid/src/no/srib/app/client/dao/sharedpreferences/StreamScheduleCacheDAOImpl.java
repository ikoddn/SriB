package no.srib.app.client.dao.sharedpreferences;

import no.srib.app.client.dao.CacheObjectDAO;
import no.srib.app.client.dao.exception.DAOException;
import no.srib.app.client.model.CacheObject;
import no.srib.app.client.model.StreamSchedule;
import android.content.Context;

import com.fasterxml.jackson.databind.JavaType;

public class StreamScheduleCacheDAOImpl extends BaseDAOImpl<StreamSchedule>
		implements CacheObjectDAO<StreamSchedule> {

	private static final String KEY = "streamSchedule";

	private final JavaType type;

	public StreamScheduleCacheDAOImpl(final Context context) {
		super(context);

		type = mapper.getTypeFactory().constructParametricType(
				CacheObject.class, StreamSchedule.class);
	}

	@Override
	public CacheObject<StreamSchedule> get() throws DAOException {
		return load(KEY, type);
	}

	@Override
	public void set(final CacheObject<StreamSchedule> cacheObject)
			throws DAOException {
		save(KEY, cacheObject);
	}
}
