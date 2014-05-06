package no.srib.sribapp.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.interfaces.DefinitionDAO;
import no.srib.sribapp.dao.interfaces.PodcastDAO;
import no.srib.sribapp.model.Definition;
import no.srib.sribapp.model.Podcast;
import no.srib.sribapp.resource.helper.PodcastBean;

@Path("podcast")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@ManagedBean
public class PodcastResource {

    @EJB
    private PodcastDAO podcastDAO;
    @EJB
    private DefinitionDAO defDAO;

    /**
     * 
     * 
     * @return A list with all recent podcasts.
     */
    @GET
    public final List<PodcastBean> getAllPodcast() {
        List<Podcast> list = null;
        List<PodcastBean> podcastList = new ArrayList<PodcastBean>();
        Map<Integer, String> programName = new HashMap<Integer, String>();
        List<Definition> defList = null;

        try {
            list = podcastDAO.getList();
            defList = defDAO.getList();

        } catch (DAOException e) {
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        }

        if (defList == null || list == null || list.isEmpty()
                || defList.isEmpty()) {
            throw new WebApplicationException(Status.NO_CONTENT);
        }

        for (Definition def : defList) {
            programName.put(def.getDefnr(), def.getName());
        }

        for (Podcast pod : list) {
            PodcastBean podBean = new PodcastBean();
            podBean.setCreatedate(pod.getCreatedate());
            podBean.setCreatetime(pod.getCreatetime());
            podBean.setDuration(pod.getDuration());
            podBean.setFilename(pod.getFilename());
            podBean.setProgram(programName.get(pod.getProgram()));
            podBean.setRefnr(pod.getRefnr());
            podBean.setRemark(pod.getRemark());
            podBean.setTitle(pod.getTitle());
            podBean.setProgramId(pod.getProgram());
            podcastList.add(podBean);
        }

        return podcastList;
    }

    // Get by programId
    @GET
    @Path("/{id}")
    public final List<PodcastBean> getPodcastByProgramId(@PathParam("id") int id) {
        List<Podcast> list = null;
        List<PodcastBean> podList = new ArrayList<PodcastBean>();
        Map<Integer, String> programName = new HashMap<Integer, String>();
        // List<Definition> defList = null;

        try {
            list = podcastDAO.getPodcasts(id);
            // defList = defDAO.getList();
        } catch (DAOException e) {
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        }

        if (list == null || list.isEmpty()) {
            throw new WebApplicationException(Status.NO_CONTENT);
        }

        for (Podcast pod : list) {
            PodcastBean podBean = new PodcastBean();
            podBean.setCreatedate(pod.getCreatedate());
            podBean.setCreatetime(pod.getCreatetime());
            podBean.setDuration(pod.getDuration());
            podBean.setFilename(pod.getFilename());
            podBean.setProgram(programName.get(pod.getProgram()));
            podBean.setRefnr(pod.getRefnr());
            podBean.setRemark(pod.getRemark());
            podBean.setTitle(pod.getTitle());
            podBean.setProgramId(pod.getProgram());
            podList.add(podBean);

        }

        return podList;

    }

    // Get all names
    @GET
    @Path("/names")
    public final List<Definition> getAllPodcastNames() {
        List<Definition> list = null;

        try {
            list = defDAO.getList();
        } catch (DAOException e) {
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        }

        if (list == null || list.isEmpty()) {
            throw new WebApplicationException(Status.NO_CONTENT);
        }

        return list;
    }

}
