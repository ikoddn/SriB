package no.srib.app.server.resource;

import java.util.ArrayList;
import java.util.Calendar;
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

import no.srib.app.server.dao.exception.DAOException;
import no.srib.app.server.dao.interfaces.DefinitionDAO;
import no.srib.app.server.dao.interfaces.PodcastDAO;
import no.srib.app.server.dao.interfaces.PrograminfoDAO;
import no.srib.app.server.model.jpa.Definition;
import no.srib.app.server.model.jpa.Podcast;
import no.srib.app.server.model.jpa.Programinfo;
import no.srib.app.server.resource.helper.PodcastBean;

@Path("podcast")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@ManagedBean
public class PodcastResource {

    private static final int DEFAULT_PODCAST_COUNT = 16;

    @EJB
    private PodcastDAO podcastDAO;
    @EJB
    private DefinitionDAO defDAO;
    @EJB
    private PrograminfoDAO programInfoDAO;

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
        List<Programinfo> programInfoList = null;
        Map<Integer, String> programPictureUrl = new HashMap<Integer, String>();

        try {
            list = podcastDAO.getList();
            defList = defDAO.getList();
            programInfoList = programInfoDAO.getList();

        } catch (DAOException e) {
            e.printStackTrace();
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        }

        if (defList == null || list == null || programInfoList == null
                || list.isEmpty() || defList.isEmpty()
                || programInfoList.isEmpty()) {
            throw new WebApplicationException(Status.NO_CONTENT);
        }

        for (Definition def : defList) {
            programName.put(def.getDefnr(), def.getName());
        }

        for (Programinfo pi : programInfoList) {
            programPictureUrl.put(pi.getProgram(), pi.getImglink());
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
            podBean.setImageUrl(programPictureUrl.get(pod.getProgram()));
            podcastList.add(podBean);
        }

        if (podcastList.size() > DEFAULT_PODCAST_COUNT) {
            podcastList = podcastList.subList(0, DEFAULT_PODCAST_COUNT);
        }

        return podcastList;
    }

    // Get by programId
    @GET
    @Path("{id}")
    public final List<PodcastBean> getPodcastByProgramId(@PathParam("id") int id) {
        List<Podcast> list = null;
        List<PodcastBean> podList = new ArrayList<PodcastBean>();
        Map<Integer, String> programName = new HashMap<Integer, String>();

        List<Programinfo> programInfoList = null;
        Map<Integer, String> programPictureUrl = new HashMap<Integer, String>();

        try {
            list = podcastDAO.getPodcasts(id);

            programInfoList = programInfoDAO.getList();
        } catch (DAOException e) {
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        }

        if (list == null || programInfoList == null || list.isEmpty()
                || programInfoList.isEmpty()) {
            throw new WebApplicationException(Status.NO_CONTENT);
        }

        for (Programinfo pi : programInfoList) {
            programPictureUrl.put(pi.getProgram(), pi.getImglink());
            programName.put(pi.getProgram(), pi.getTitle());
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
            podBean.setImageUrl(programPictureUrl.get(pod.getProgram()));
            podList.add(podBean);

        }

        return podList;

    }

    // Get all names
    @GET
    @Path("names")
    public final List<Definition> getAllPodcastNames() {
        List<Definition> defList = new ArrayList<Definition>();
        List<Programinfo> programInfoListNew = null;
        List<Programinfo> programInfoListOld = null;

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -6);
        try {
            programInfoListNew = programInfoDAO.getProgramInfosWithPodcast(cal,
                    true);
            programInfoListOld = programInfoDAO.getProgramInfosWithPodcast(cal,
                    false);

        } catch (DAOException e) {

            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        }

        programInfoListNew.addAll(programInfoListOld);
        System.out.println("Størrelse: " + programInfoListNew.size());
        if (programInfoListNew == null || programInfoListNew.isEmpty()) {
            System.out.println("NO CONTENT");
            throw new WebApplicationException(Status.NO_CONTENT);
        }

        for (Programinfo prog : programInfoListNew) {
            Definition def = new Definition();
            def.setDefnr(prog.getProgram());
            def.setName(prog.getTitle());
            defList.add(def);

        }

        return defList;
    }

}
