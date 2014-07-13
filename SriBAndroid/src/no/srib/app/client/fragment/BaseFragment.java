package no.srib.app.client.fragment;

import no.srib.app.client.util.BusProvider;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		BusProvider.INSTANCE.get().post(this);
	}
}
