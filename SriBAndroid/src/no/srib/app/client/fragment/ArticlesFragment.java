package no.srib.app.client.fragment;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.srib.R;
import no.srib.app.client.asynctask.HttpAsyncTask;
import no.srib.app.client.asynctask.HttpAsyncTask.HttpResponseListener;
import no.srib.app.client.model.NewsArticle;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ArticlesFragment extends Fragment {

	private final ObjectMapper MAPPER;

	private TextView label;
	private ListView listView;
	private List<NewsArticle> list;
	private ArticleListAdapter listAdapter;

	public ArticlesFragment() {
		MAPPER = new ObjectMapper();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_articles, container,
				false);

		list = null;

		label = (TextView) rootView.findViewById(R.id.label_articles);
		label.setText("Articles fragment");

		listAdapter = new ArticleListAdapter();

		listView = (ListView) rootView.findViewById(R.id.listview_articles);
		listView.setAdapter(listAdapter);

		HttpAsyncTask httpAsyncTask = new HttpAsyncTask(
				new ArticleHttpResponseListener());
		String url = getResources().getString(R.string.url_articles);
		httpAsyncTask.execute(url);

		return rootView;
	}

	private class ArticleHttpResponseListener implements HttpResponseListener {

		@Override
		public void onResponse(String response) {
			if (response != null) {
				try {
					list = MAPPER.readValue(response,
							new TypeReference<List<NewsArticle>>() {
							});
					listAdapter.notifyDataSetChanged();
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
	}

	private class ArticleListAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		public ArticleListAdapter() {
			inflater = getActivity().getLayoutInflater();
		}

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
		}

		@Override
		public NewsArticle getItem(int position) {
			if (list != null) {
				return list.get(position);
			} else {
				return null;
			}
		}

		@Override
		public long getItemId(int position) {
			if (list != null) {
				return list.get(position).getId();
			} else {
				return 0;
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = inflater.inflate(R.layout.listitem_article, null);

			ImageView image = (ImageView) convertView
					.findViewById(R.id.imageview_article);
			TextView title = (TextView) convertView
					.findViewById(R.id.textview_article_title);
			TextView excerpt = (TextView) convertView
					.findViewById(R.id.textview_article_excerpt);

			NewsArticle newsArticle = list.get(position);

			image.setImageDrawable(convertView.getResources().getDrawable(
					R.drawable.frank));
			title.setText(newsArticle.getTitle());
			excerpt.setText(newsArticle.getExcerptDisplay());

			return convertView;
		}
	}
}
