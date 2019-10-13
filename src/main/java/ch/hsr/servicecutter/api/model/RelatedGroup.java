package ch.hsr.servicecutter.api.model;

import java.util.List;

public class RelatedGroup {

	private String name;
	private List<String> nanoentities;

	// Jackson
	public RelatedGroup() {
	}

	public List<String> getNanoentities() {
		return nanoentities;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setNanoentities(final List<String> nanoentities) {
		this.nanoentities = nanoentities;
	}

}
