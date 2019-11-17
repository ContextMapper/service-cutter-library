package ch.hsr.servicecutter.api.model;

import java.util.Collections;
import java.util.Set;

public class ServiceRelation {

	public enum Direction {
		OUTGOING, // service A to service B
		INCOMING, // the other way round
		BIDIRECTIONAL
	}

	private String serviceA;
	private String serviceB;
	private Set<String> sharedEntities;
	private Direction direction;

	public ServiceRelation() {
		this.sharedEntities = Collections.emptySet();
	}

	public ServiceRelation(final Set<String> sharedEntities, final String serviceA, final String serviceB, final Direction direction) {
		this.sharedEntities = sharedEntities;
		this.serviceA = serviceA;
		this.serviceB = serviceB;
		this.direction = direction;
	}

	public void setSharedEntities(Set<String> sharedEntities) {
		this.sharedEntities = sharedEntities;
	}

	public Set<String> getSharedEntities() {
		return sharedEntities;
	}

	public void setServiceA(String serviceA) {
		this.serviceA = serviceA;
	}

	public String getServiceA() {
		return serviceA;
	}

	public void setServiceB(String serviceB) {
		this.serviceB = serviceB;
	}

	public String getServiceB() {
		return serviceB;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public Direction getDirection() {
		return direction;
	}

}
