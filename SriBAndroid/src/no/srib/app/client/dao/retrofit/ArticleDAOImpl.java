package no.srib.app.client.dao.retrofit;

import java.io.IOException;
import java.util.List;

import no.srib.app.client.dao.ArticleDAO;
import no.srib.app.client.dao.exception.DAOException;
import no.srib.app.client.model.Article;

import org.apache.http.HttpStatus;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ArticleDAOImpl implements ArticleDAO {

	private final JavaType type;
	private final ObjectMapper mapper;

	private AppServerService appServer;

	public ArticleDAOImpl(final String restApiUrl) {
		mapper = new ObjectMapper();
		type = mapper.getTypeFactory().constructCollectionType(List.class,
				Article.class);

		RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(
				restApiUrl).build();

		appServer = restAdapter.create(AppServerService.class);
	}

	@Override
	public List<Article> getList() throws DAOException {
		return getList(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see no.srib.app.client.dao.ArticleDAO#getList(java.lang.String)
	 * 
	 * Returns null on 204 No content.
	 */
	@Override
	public List<Article> getList(final String query) throws DAOException {
		List<Article> list = null;
		Response response = null;

		try {
			response = appServer.getArticles(query);
		} catch (RetrofitError e) {
			throw new DAOException(e);
		}

		if (response != null && response.getStatus() == HttpStatus.SC_OK) {
			try {
				list = mapper.readValue(response.getBody().in(), type);
			} catch (JsonParseException e) {
				throw new DAOException(e);
			} catch (JsonMappingException e) {
				throw new DAOException(e);
			} catch (IOException e) {
				throw new DAOException(e);
			}
		}

		return list;
	}
}
