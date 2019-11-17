package ch.hsr.servicecutter.api.model;

import java.util.Collections;
import java.util.List;

import com.google.common.base.Objects;

public class Service {

	private List<String> nanoentities;
	private char id;

	public Service() {
		this.nanoentities = Collections.emptyList();
	}

	public void setNanoentities(List<String> nanoentities) {
		this.nanoentities = nanoentities;
	}

	public List<String> getNanoentities() {
		return nanoentities;
	}

	public String getName() {
		return "Service " + id;
	}

	public void setId(char id) {
		this.id = id;
	}

	public char getId() {
		return id;
	}
}
