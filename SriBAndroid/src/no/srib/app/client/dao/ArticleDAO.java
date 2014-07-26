package no.srib.app.client.dao;

import java.util.List;

import no.srib.app.client.dao.exception.DAOException;
import no.srib.app.client.model.Article;

public interface ArticleDAO {

	List<Article> getList() throws DAOException;
	
	List<Article> getList(String query) throws DAOException;
}
