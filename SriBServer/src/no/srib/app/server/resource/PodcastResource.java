package no.srib.app.server.resource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
import no.srib.app.server.model.json.PodcastBean;
import no.srib.app.server.model.json.PodcastPrograms;
import no.srib.app.server.util.DefinitionNameComparator;
import no.srib.app.server.util.ModelUtil;

@Path("podcast")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@ManagedBean
public class PodcastResource {

    private static final int DEFAULT_NEWER_MONTH_LIMIT = 6;

    @EJB
    private PodcastDAO podcastDAO;
    @EJB
    private DefinitionDAO defDAO;
    @EJB
    private PrograminfoDAO programInfoDAO;

    /**
     * 
     * @return A list with all recent podcasts.
     */
    @GET
    public final List<PodcastBean> getPodcasts(
            @DefaultValue("16") @QueryParam("c") final int count) {

        if (count <= 0) {
            throw new WebApplicationException(Status.BAD_REQUEST);
        }

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

        for (Podcast podcast : list) {
            int programId = podcast.getProgram();

            PodcastBean podBean = ModelUtil.toPodcastBean(podcast);
            podBean.setProgram(programName.get(programId));
            podBean.setImageUrl(programPictureUrl.get(programId));

            podcastList.add(podBean);
        }

        if (podcastList.size() > count) {
            podcastList = podcastList.subList(0, count);
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

        for (Podcast podcast : list) {
            int programId = podcast.getProgram();

            PodcastBean podBean = ModelUtil.toPodcastBean(podcast);
            podBean.setProgram(programName.get(programId));
            podBean.setImageUrl(programPictureUrl.get(programId));

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
            Definition def = new Definition(prog.getProgram(), prog.getTitle());
            defList.add(def);
        }

        return defList;
    }

    @GET
    @Path("programs")
    public final PodcastPrograms getPodcastPrograms(
            @QueryParam("d") final int dateParam) {

        int date;

        if (dateParam == 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -DEFAULT_NEWER_MONTH_LIMIT);

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            StringBuilder sb = new StringBuilder();
            sb.append(year);
            sb.append(String.format("%02d", month));
            sb.append(String.format("%02d", day));
            String dateString = sb.toString();

            date = Integer.parseInt(dateString);
        } else if (dateParam > 0) {
            date = dateParam;
        } else {
            throw new WebApplicationException(Status.BAD_REQUEST);
        }

        Locale locale = new Locale("no");
        Comparator<Definition> comparator = new DefinitionNameComparator(locale);
        SortedSet<Definition> newer = new TreeSet<Definition>(comparator);
        SortedSet<Definition> older = new TreeSet<Definition>(comparator);

        try {
            List<Podcast> latestPodcasts = podcastDAO
                    .getLatestPodcastOfEachProgram();

            for (Podcast latestPodcast : latestPodcasts) {
                int programId = latestPodcast.getProgram();
                Programinfo programinfo = programInfoDAO.getById(programId);
                String name = null;
                Definition definition = null;

                if (programinfo != null) {
                    name = programinfo.getTitle();
                    definition = new Definition(programId, name);
                } else {
                    definition = defDAO.getById(programId);
                }

                if (definition != null) {
                    if (latestPodcast.getCreatedate() > date) {
                        newer.add(definition);
                    } else {
                        older.add(definition);
                    }
                }
            }
        } catch (DAOException e) {
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        }

        return new PodcastPrograms(newer, older);
    }
}
