package ch.hsr.servicecutter.api.model;

import java.util.List;

public class Characteristic {

	private String characteristic;
	private List<String> nanoentities;

	public Characteristic() {
	}

	public String getCharacteristic() {
		return characteristic;
	}

	public void setCharacteristic(final String characteristic) {
		this.characteristic = characteristic;
	}

	public List<String> getNanoentities() {
		return nanoentities;
	}

	public void setNanoentities(final List<String> nanoentities) {
		this.nanoentities = nanoentities;
	}

}
