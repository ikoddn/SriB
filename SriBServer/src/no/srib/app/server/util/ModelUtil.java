package no.srib.app.server.util;

import no.srib.app.server.model.jpa.Podcast;
import no.srib.app.server.model.json.PodcastBean;

public class ModelUtil {

    public static PodcastBean toPodcastBean(final Podcast podcast) {
        return new PodcastBean(podcast.getRefnr(), podcast.getCreatedate(),
                podcast.getCreatetime(), podcast.getDuration(),
                podcast.getProgram(), podcast.getFilename(),
                podcast.getRemark(), podcast.getTitle());
    }
}
