package no.srib.app.client.dao;

import no.srib.app.client.dao.exception.DAOException;
import no.srib.app.client.model.PodcastPrograms;

public interface PodcastProgramsDAO {

	PodcastPrograms get() throws DAOException;
}
