package no.srib.app.client.adapter;

import no.srib.app.client.R;
import no.srib.app.client.model.ProgramName;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ProgramSpinnerAdapter extends BaseListAdapter<ProgramName> {

	enum ViewType {
		GENERAL,
		DIVIDER,
		PROGRAM
	}

	/**
	 * 1: Velg program
	 */
	private static final int GENERAL_ITEMS_COUNT = 1;

	/**
	 * 1: Between general items and newer podcast programs <br>
	 * 2: Between newer podcast programs and older podcast programs
	 */
	private static final int DIVIDER_COUNT = 2;

	private int newerCount;
	private LayoutInflater inflater;
	private Typeface font;

	public ProgramSpinnerAdapter(final LayoutInflater inflater,
			final Typeface font) {

		newerCount = 0;
		this.inflater = inflater;
		this.font = font;
	}

	public void setNewerCount(final int newerCount) {
		this.newerCount = newerCount;
	}

	@Override
	public int getCount() {
		int listSize = super.getCount();
		return listSize == 0 ? 0 : listSize + DIVIDER_COUNT;
	}

	@Override
	public ProgramName getItem(final int position) {
		int divider1Pos = GENERAL_ITEMS_COUNT;
		int divider2Pos = divider1Pos + newerCount + 1;

		if (position < divider1Pos) {
			return super.getItem(position);
		} else if (position == divider1Pos) {
			return null;
		} else if (position < divider2Pos) {
			return super.getItem(position - 1);
		} else if (position == divider2Pos) {
			return null;
		} else {
			return super.getItem(position - 2);
		}
	}

	@Override
	public long getItemId(final int position) {
		ProgramName item = getItem(position);
		return item == null ? 0 : item.getDefnr();
	}

	private boolean isDivider(final int position) {
		int divider1Pos = GENERAL_ITEMS_COUNT;
		int divider2Pos = divider1Pos + newerCount + 1;

		return position == divider1Pos || position == divider2Pos;
	}

	@Override
	public boolean isEnabled(final int position) {
		return !isDivider(position);
	}

	@Override
	public int getViewTypeCount() {
		return ViewType.values().length;
	}

	@Override
	public int getItemViewType(final int position) {
		int divider1Pos = GENERAL_ITEMS_COUNT;

		if (position < divider1Pos) {
			return ViewType.GENERAL.ordinal();
		} else if (isDivider(position)) {
			return ViewType.DIVIDER.ordinal();
		} else {
			return ViewType.PROGRAM.ordinal();
		}
	}

	@Override
	public View getDropDownView(final int position, final View convertView,
			final ViewGroup parent) {
		View view = convertView;

		int divider1Pos = GENERAL_ITEMS_COUNT;
		int divider2Pos = divider1Pos + newerCount + 1;

		int itemViewType = getItemViewType(position);

		// Workaround for reusing convertView since a Spinner does not support
		// multiple view types:
		// https://code.google.com/p/android/issues/detail?id=17128
		boolean mustInflate = view == null
				|| (Integer) view.getTag() != itemViewType;

		if (position < divider1Pos) {
			if (mustInflate) {
				view = inflater.inflate(R.layout.spinneritem_podcast_default,
						null);
			}

			TextView text = (TextView) view
					.findViewById(R.id.textView_programItem_default);
			text.setTypeface(font);
			text.setText(getItem(position).getName());
		} else if (position == divider1Pos) {
			if (mustInflate) {
				view = inflater.inflate(R.layout.spinneritem_podcast_divider,
						null);
			}

			ImageView imageView = (ImageView) view
					.findViewById(R.id.imageView_programItem_divider);
			imageView.setImageResource(R.drawable.list_divider_1);

			TextView text = (TextView) view
					.findViewById(R.id.textView_programItem_divider);
			text.setTypeface(font);
			text.setText(R.string.spinner_podcast_newer);
		} else if (position == divider2Pos) {
			if (mustInflate) {
				view = inflater.inflate(R.layout.spinneritem_podcast_divider,
						null);
			}

			ImageView imageView = (ImageView) view
					.findViewById(R.id.imageView_programItem_divider);
			imageView.setImageResource(R.drawable.list_divider_2);

			TextView text = (TextView) view
					.findViewById(R.id.textView_programItem_divider);
			text.setTypeface(font);
			text.setText(R.string.spinner_podcast_older);
		} else {
			if (mustInflate) {
				view = inflater.inflate(R.layout.spinneritem_podcast_program,
						null);
			}

			TextView text = (TextView) view
					.findViewById(R.id.textView_programItem_program);
			text.setTypeface(font);
			text.setText(getItem(position).getName());
		}

		if (mustInflate) {
			view.setTag(Integer.valueOf(itemViewType));
		}

		return view;
	}

	@Override
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {

		View view = convertView;

		if (view == null) {
			view = inflater.inflate(R.layout.spinneritem_podcast_default, null);
		}

		TextView text = (TextView) view
				.findViewById(R.id.textView_programItem_default);
		text.setTypeface(font);
		text.setText(getItem(position).getName());

		return view;
	}
}
