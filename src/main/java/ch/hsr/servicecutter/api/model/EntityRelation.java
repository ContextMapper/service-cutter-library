package ch.hsr.servicecutter.api.model;

public class EntityRelation {
	private Entity origin;
	private Entity destination;
	private RelationType type;

	// used by Jackson
	public EntityRelation() {
	}

	public void setDestination(Entity destination) {
		this.destination = destination;
	}

	public void setOrigin(Entity origin) {
		this.origin = origin;
	}

	public void setType(RelationType type) {
		this.type = type;
	}

	public Entity getDestination() {
		return destination;
	}

	public Entity getOrigin() {
		return origin;
	}

	public RelationType getType() {
		return type;
	}

	public static enum RelationType {
		AGGREGATION, COMPOSITION, INHERITANCE
	}

}
