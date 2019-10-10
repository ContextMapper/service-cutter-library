package ch.hsr.servicecutter.model.usersystem;

import com.google.common.base.Objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserSystem {

	private Long id;
	private String name;

	private List<Nanoentity> nanoentities = new ArrayList<>();

	private List<CouplingInstance> couplingInstances = new ArrayList<>();

	public List<Nanoentity> getNanoentities() {
		return Collections.unmodifiableList(nanoentities);
	}

	public void addNanoentity(final Nanoentity nanoentity) {
		nanoentities.add(nanoentity);
		nanoentity.setUserSystem(this);
	}

	public void addCouplingInstance(final CouplingInstance instance) {
		couplingInstances.add(instance);
		instance.setSystem(this);
	}

	public List<CouplingInstance> getCouplingInstances() {
		return couplingInstances;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof UserSystem) {
			UserSystem other = (UserSystem) obj;
			return this == other || Objects.equal(id, other.id);
		} else {
			return false;
		}
	}

}
