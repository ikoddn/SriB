package no.srib.app.server.model.json;

import java.util.SortedSet;

import no.srib.app.server.model.jpa.Definition;

public class PodcastPrograms {

    private SortedSet<Definition> newer;
    private SortedSet<Definition> older;

    protected PodcastPrograms() {
    }

    public PodcastPrograms(final SortedSet<Definition> newer,
            final SortedSet<Definition> older) {
        this.newer = newer;
        this.older = older;
    }

    public SortedSet<Definition> getNewer() {
        return newer;
    }

    public void setNewer(final SortedSet<Definition> newer) {
        this.newer = newer;
    }

    public SortedSet<Definition> getOlder() {
        return older;
    }

    public void setOlder(final SortedSet<Definition> older) {
        this.older = older;
    }
}
