package no.srib.app.client.adapter.updater;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import no.srib.app.client.adapter.ListBasedAdapter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonAdapterUpdater<T> implements AdapterUpdater<T, String> {

	private final JavaType LIST_TYPE;
	private final ObjectMapper MAPPER;
	private final ListBasedAdapter<T> ADAPTER;
	private T defaultValue;
	private List<T> storedData;
	private AtomicBoolean didStoreData;

	public JsonAdapterUpdater(final Class<T> typeClass,
			final ListBasedAdapter<T> adapter) {
		MAPPER = new ObjectMapper();
		LIST_TYPE = MAPPER.getTypeFactory().constructCollectionType(List.class,
				typeClass);
		this.ADAPTER = adapter;
		defaultValue = null;
		storedData = null;
		didStoreData = new AtomicBoolean(false);
	}

	@Override
	public void setDefaultValue(final T defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public void updateFrom(final String json, final boolean store) {
		try {
			List<T> list = MAPPER.readValue(json, LIST_TYPE);

			if (defaultValue != null) {
				list.add(0, defaultValue);
			}

			if (store) {
				storedData = list;
			}
			
			didStoreData.set(store);

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

	@Override
	public void restoreStoredData() {
		if (!didStoreData.get()) {
			ADAPTER.setList(storedData);
			ADAPTER.notifyDataSetChanged();
		}
	}
}
