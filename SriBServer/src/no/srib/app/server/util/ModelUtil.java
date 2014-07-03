package no.srib.app.server.util;

import no.srib.app.server.model.jpa.Podcast;
import no.srib.app.server.model.jpa.Schedule;
import no.srib.app.server.model.json.PodcastBean;
import no.srib.app.server.model.json.ScheduleBean;

public class ModelUtil {

    public static PodcastBean toPodcastBean(final Podcast podcast) {
        return new PodcastBean(podcast.getRefnr(), podcast.getCreatedate(),
                podcast.getCreatetime(), podcast.getDuration(),
                podcast.getProgram(), podcast.getFilename(),
                podcast.getRemark(), podcast.getTitle());
    }

    public static ScheduleBean toScheduleBean(final Schedule schedule) {
        return new ScheduleBean(schedule.getId(), schedule.getDay(),
                schedule.getFromtime(), schedule.getTotime());
    }
}
