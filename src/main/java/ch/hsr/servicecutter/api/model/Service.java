package ch.hsr.servicecutter.api.model;

import java.util.List;

import com.google.common.base.Objects;

public class Service {

	private List<String> nanoentities;
	private char id;

	public Service(final List<String> nanoentities, final char id) {
		this.nanoentities = nanoentities;
		this.id = id;
	}

	public List<String> getNanoentities() {
		return nanoentities;
	}

	public String getName() {
		return "Service " + id;
	}

}
