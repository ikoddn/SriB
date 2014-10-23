package no.srib.app.client.adapter;

import no.srib.app.client.R;
import no.srib.app.client.model.ProgramName;
import no.srib.app.client.view.DividerView;
import no.srib.app.client.view.ProgramSpinnerView;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class ProgramSpinnerAdapter extends ListBasedAdapter<ProgramName> {

	enum Type {
		GENERAL,
		DIVIDER,
		SUBHEADER,
		PROGRAM
	}

	private static final int TYPE_COUNT = Type.values().length;
	private static final int[] GENERAL_ITEM_IDS = {
			R.string.spinner_podcast_all,
			R.string.spinner_podcast_downloaded
	};
	private static final int[] SUBHEADER_IDS = {
			R.string.spinner_podcast_newer, R.string.spinner_podcast_older };

	private final Context context;
	private final String[] generalItems;
	private final String[] subheaders;

	private int newerCount;

	public ProgramSpinnerAdapter(final Context context) {
		this.context = context;

		generalItems = new String[GENERAL_ITEM_IDS.length];
		for (int i = 0; i < generalItems.length; ++i) {
			generalItems[i] = context.getString(GENERAL_ITEM_IDS[i]);
		}

		subheaders = new String[SUBHEADER_IDS.length];
		for (int i = 0; i < subheaders.length; ++i) {
			subheaders[i] = context.getString(SUBHEADER_IDS[i]);
		}

		newerCount = 0;
	}

	public void setNewerCount(final int newerCount) {
		this.newerCount = newerCount;
	}

	@Override
	public int getCount() {
		int listSize = super.getCount();
		return listSize == 0 ? 0 : listSize + generalItems.length + 2
				* subheaders.length;
	}

	@Override
	public ProgramName getItem(final int position) {
		int divider1Pos = generalItems.length;
		int divider2Pos = divider1Pos + newerCount + 2;
		int subheader1Pos = divider1Pos + 1;
		int subheader2Pos = divider2Pos + 1;

		if (position <= subheader1Pos) {
			return null;
		} else if (position < divider2Pos) {
			return super.getItem(position - generalItems.length - 2);
		} else if (position <= subheader2Pos) {
			return null;
		} else {
			return super.getItem(position - generalItems.length - 4);
		}
	}

	@Override
	public long getItemId(final int position) {
		ProgramName item = getItem(position);
		return item == null ? 0 : item.getDefnr();
	}

	@Override
	public boolean isEnabled(final int position) {
		Type type = getItemViewEnumType(position);
		return type != Type.DIVIDER && type != Type.SUBHEADER;
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_COUNT;
	}

	private Type getItemViewEnumType(final int position) {
		int divider1Pos = generalItems.length;
		int divider2Pos = divider1Pos + newerCount + 2;
		int subheader1Pos = divider1Pos + 1;
		int subheader2Pos = divider2Pos + 1;

		if (position < divider1Pos) {
			return Type.GENERAL;
		} else if (position == divider1Pos || position == divider2Pos) {
			return Type.DIVIDER;
		} else if (position == subheader1Pos || position == subheader2Pos) {
			return Type.SUBHEADER;
		} else {
			return Type.PROGRAM;
		}
	}

	@Override
	public int getItemViewType(final int position) {
		return getItemViewEnumType(position).ordinal();
	}

	private int dividerNumber(final int position) {
		int divider1Pos = generalItems.length;
		int divider2Pos = divider1Pos + newerCount + 2;

		if (position == divider1Pos) {
			return 0;
		} else if (position == divider2Pos) {
			return 1;
		} else {
			return -1;
		}
	}

	@Override
	public View getDropDownView(final int position, final View convertView,
			final ViewGroup parent) {

		View resultView;

		Type itemViewType = getItemViewEnumType(position);

		// Workaround for reusing convertView since a Spinner does not support
		// multiple view types:
		// https://code.google.com/p/android/issues/detail?id=17128
		boolean mustInflate = convertView == null
				|| (Type) convertView.getTag() != itemViewType;

		int dividerNumber = dividerNumber(position);

		if (dividerNumber != -1) { // Divider
			DividerView view;

			if (mustInflate) {
				view = new DividerView(context);
			}
			else {
				view = (DividerView) convertView;
			}

			view.showDivider(dividerNumber);
			resultView = view;
		} else { // Not a divider
			ProgramSpinnerView view = null;
			int layoutId = 0;
			String text = null;

			switch (itemViewType) {
			case GENERAL:
				layoutId = R.layout.spinneritem_podcast_default;
				text = generalItems[position];
				break;
			case PROGRAM:
				layoutId = R.layout.spinneritem_podcast_indented;
				text = getItem(position).getName();
				break;
			case SUBHEADER:
				layoutId = R.layout.spinneritem_podcast_greyedout;
				text = subheaders[dividerNumber(position - 1)];
				break;
			default:
				break;
			}

			if (mustInflate) {
				view = new ProgramSpinnerView(context);
				view.init(layoutId);
			} else {
				view = (ProgramSpinnerView) convertView;
			}

			view.showText(text);
			resultView = view;
		}

		if (mustInflate) {
			resultView.setTag(itemViewType);
		}

		// hide the choose program item
//		if(position == 0)
//			resultView.setVisibility(View.GONE);

		return resultView;
	}

	@Override
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {

		ProgramSpinnerView view;

		if (convertView == null) {
			view = new ProgramSpinnerView(context);
			view.init(R.layout.spinneritem_podcast_default);
		} else {
			view = (ProgramSpinnerView) convertView;
		}

		String text;
		if (position < generalItems.length) {
			text = generalItems[position];
		} else {
			text = getItem(position).getName();
		}

		if(position == 0)
			view.showText(context.getResources().getString(R.string.spinner_podcast_default));
		else
			view.showText(text);

		return view;
	}
}
