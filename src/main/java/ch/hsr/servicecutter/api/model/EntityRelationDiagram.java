package ch.hsr.servicecutter.api.model;

import java.util.List;

public class EntityRelationDiagram {
    String name;
    List<Entity> entities;
    List<EntityRelation> relations;

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public List<EntityRelation> getRelations() {
        return relations;
    }

    public void setRelations(List<EntityRelation> relations) {
        this.relations = relations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
