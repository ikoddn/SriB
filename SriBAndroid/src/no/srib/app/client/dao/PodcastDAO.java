package no.srib.app.client.dao;

import java.util.List;

import no.srib.app.client.dao.exception.DAOException;
import no.srib.app.client.model.Podcast;

public interface PodcastDAO {

	List<Podcast> getList() throws DAOException;

	List<Podcast> getListFromProgram(int programId) throws DAOException;
}
