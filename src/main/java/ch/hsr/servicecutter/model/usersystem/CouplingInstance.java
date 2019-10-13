package ch.hsr.servicecutter.model.usersystem;

import ch.hsr.servicecutter.model.criteria.CouplingCriterion;
import ch.hsr.servicecutter.model.criteria.CouplingCriterionCharacteristic;
import ch.hsr.servicecutter.model.criteria.CouplingType;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CouplingInstance implements Comparable<CouplingInstance> {

	private String name;
	private List<Nanoentity> nanoentities = new ArrayList<>();
	private CouplingCriterion couplingCriterion;
	private CouplingCriterionCharacteristic characteristic;
	private List<Nanoentity> secondNanoentities = new ArrayList<>();
	private InstanceType instanceType;

	public CouplingInstance(final CouplingCriterion couplingCriterion, final InstanceType type) {
		Assert.assertNotEquals("Constructor only to be used for not-compatibility criteria!", CouplingType.COMPATIBILITY, couplingCriterion.getType());
		instanceType = type;
		this.couplingCriterion = couplingCriterion;
	}

	public CouplingInstance(final CouplingCriterionCharacteristic characteristic, final InstanceType type) {
		instanceType = type;
		setCharacteristicAndCriterion(characteristic);
	}

	public CouplingInstance() {
	}

	public List<Nanoentity> getNanoentities() {
		return Collections.unmodifiableList(nanoentities);
	}

	public void addNanoentity(final Nanoentity nanoentity) {
		nanoentities.add(nanoentity);
	}

	public void setNanoentities(final Collection<Nanoentity> nanoentities) {
		this.nanoentities.clear();
		if (nanoentities != null) {
			this.nanoentities.addAll(nanoentities);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public CouplingCriterion getCouplingCriterion() {
		return couplingCriterion;
	}

	public CouplingCriterionCharacteristic getCharacteristic() {
		return characteristic;
	}

	public List<Nanoentity> getSecondNanoentities() {
		return Collections.unmodifiableList(secondNanoentities);
	}

	public void setSecondNanoentities(final List<Nanoentity> secondNanoentities) {
		this.secondNanoentities.clear();
		if (secondNanoentities != null) {
			this.secondNanoentities.addAll(secondNanoentities);
		}
	}

	public List<Nanoentity> getAllNanoentities() {
		List<Nanoentity> result = new ArrayList<>();
		result.addAll(nanoentities);
		result.addAll(secondNanoentities);
		return result;
	}

	private void setCharacteristicAndCriterion(final CouplingCriterionCharacteristic characteristic) {
		Assert.assertNotNull(characteristic);
		this.characteristic = characteristic;
		this.couplingCriterion = characteristic.getCouplingCriterion();
	}

	@Override
	public int compareTo(final CouplingInstance o) {
		return couplingCriterion.getName().compareTo(o.getCouplingCriterion().getName());
	}

	public InstanceType getType() {
		return instanceType;
	}

}
