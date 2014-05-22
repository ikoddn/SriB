package no.srib.app.client.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public abstract class SectionFragment extends Fragment {

	public abstract int getFrameLayoutID();

	public abstract Fragment getBaseFragment();

	public void pushFragment(Fragment fragment) {
		FragmentManager manager = getChildFragmentManager();

		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(getFrameLayoutID(), fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	public boolean popFragment() {
		FragmentManager manager = getChildFragmentManager();

		if (manager.getBackStackEntryCount() > 0) {
			manager.popBackStack();
			return true;
		}

		return false;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		FragmentManager manager = getChildFragmentManager();

		if (manager.getBackStackEntryCount() == 0) {
			FragmentTransaction transaction = getChildFragmentManager()
					.beginTransaction();
			transaction.replace(getFrameLayoutID(), getBaseFragment());
			transaction.commit();
		}
	}
}
