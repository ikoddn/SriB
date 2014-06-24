package no.srib.app.client.adapter.updater;

import java.io.IOException;
import java.util.List;

import no.srib.app.client.adapter.BaseListAdapter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonAdapterUpdater<T> implements AdapterUpdater<T, String> {

	private final JavaType LIST_TYPE;
	private final ObjectMapper MAPPER;
	private final BaseListAdapter<T> ADAPTER;
	private T defaultValue;

	public JsonAdapterUpdater(final Class<T> typeClass,
			final BaseListAdapter<T> adapter) {
		MAPPER = new ObjectMapper();
		LIST_TYPE = MAPPER.getTypeFactory().constructCollectionType(List.class,
				typeClass);
		this.ADAPTER = adapter;
		defaultValue = null;
	}

	@Override
	public void setDefaultValue(final T defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public void updateFrom(final String json) {
		try {
			List<T> list = MAPPER.readValue(json, LIST_TYPE);

			if (defaultValue != null) {
				list.add(0, defaultValue);
			}

			ADAPTER.setList(list);
			ADAPTER.notifyDataSetChanged();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
