package no.srib.app.client.model;

import java.util.List;

public class PodcastPrograms {

	private List<ProgramName> newer;
	private List<ProgramName> older;

	protected PodcastPrograms() {
	}

	public List<ProgramName> getNewer() {
		return newer;
	}

	public void setNewer(List<ProgramName> newer) {
		this.newer = newer;
	}

	public List<ProgramName> getOlder() {
		return older;
	}

	public void setOlder(List<ProgramName> older) {
		this.older = older;
	}
}
