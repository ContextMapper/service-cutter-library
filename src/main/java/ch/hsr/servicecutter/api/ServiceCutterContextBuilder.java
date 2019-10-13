/*
 * Copyright 2019 The Context Mapper Project Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.hsr.servicecutter.api;

import ch.hsr.servicecutter.api.model.*;
import ch.hsr.servicecutter.model.criteria.CouplingCriterion;
import ch.hsr.servicecutter.model.criteria.CouplingCriterionCharacteristic;
import ch.hsr.servicecutter.model.criteria.CouplingType;
import ch.hsr.servicecutter.model.usersystem.CouplingInstance;
import ch.hsr.servicecutter.model.usersystem.InstanceType;
import ch.hsr.servicecutter.model.usersystem.Nanoentity;
import ch.hsr.servicecutter.solver.SolverConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Builder to create the ServiceCutterContext needed to call the ServiceCutter solver.
 *
 * @author Stefan Kapferer
 */
public class ServiceCutterContextBuilder {

    private final Logger log = LoggerFactory.getLogger(ServiceCutterContextBuilder.class);

    private ServiceCutterContext context;

    private EntityRelationDiagram entityRelationDiagram;
    private UserRepresentationContainer userRepresentationContainer;
    private SolverConfiguration solverConfiguration;
    private Long nanoEntityId = 0L;

    /**
     * An EntityRelationDiagram instance describing your system is always required. Construct it manually or use the
     * {@link EntityRelationDiagramImporterJSON} class to import it with a JSON file.
     */
    public ServiceCutterContextBuilder(EntityRelationDiagram entityRelationDiagram) {
        this.entityRelationDiagram = entityRelationDiagram;
    }

    /**
     * Optionally you can use User Representations which may improve your results.
     * Construct them manually or use the {@link UserRepresentationContainerImporterJSON} class to import them from
     * a JSON file.
     */
    public ServiceCutterContextBuilder withUserRepresentations(UserRepresentationContainer userRepresentations) {
        this.userRepresentationContainer = userRepresentations;
        return this;
    }

    /**
     * Change the solver configuration. If not called, we use the default configuration. {@link SolverConfigurationFactory}
     * allows to create one with a JSON file as input.
     */
    public ServiceCutterContextBuilder withCustomSolverConfiguration(SolverConfiguration solverConfiguration) {
        this.solverConfiguration = solverConfiguration;
        return this;
    }

    /**
     * Builds the {@link ServiceCutterContext}
     */
    public ServiceCutterContext build() {
        context = new ServiceCutterContext(entityRelationDiagram.getName());
        initSolverConfiguration();
        initERDRelatedData();
        initUserRepresentationRelatedData();
        return context;
    }

    private void initSolverConfiguration() {
        if (this.solverConfiguration == null) {
            context.setSolverConfiguration(new SolverConfigurationFactory().createDefaultConfiguration());
        } else {
            context.setSolverConfiguration(this.solverConfiguration);
        }
    }

    /**
     * Basically the ERD import in original ServiceCutter
     */
    private void initERDRelatedData() {
        String name = entityRelationDiagram.getName();
        List<TemporaryNanoentity> nanoentities = entityRelationDiagram.getEntities().stream().flatMap(e -> e.getNanoentities().stream().map(n -> new TemporaryNanoentity(e.getName(), n))).collect(toList());

        // entities
        CouplingCriterion criterion = context.getCriteriaCatalog().getCriterionByName(CouplingCriterion.IDENTITY_LIFECYCLE);
        Map<String, List<TemporaryNanoentity>> nanoentitiesByEntity = expandEntitiesByCompositionAndInheritance(entityRelationDiagram, nanoentities).stream()
                .collect(Collectors.groupingBy(TemporaryNanoentity::getNewEntity));
        for (Map.Entry<String, List<TemporaryNanoentity>> entityAndNanoentities : nanoentitiesByEntity.entrySet()) {
            CouplingInstance entityCoupling = new CouplingInstance(criterion, InstanceType.SAME_ENTITY);
            context.addCouplingInstance(entityCoupling);
            entityCoupling.setName(entityAndNanoentities.getKey());
            for (TemporaryNanoentity newNanoentity : entityAndNanoentities.getValue()) {
                entityCoupling.addNanoentity(createNanoentity(newNanoentity.getOriginalEntity(), newNanoentity.getOriginalName()));
            }
        }

        // Aggregations
        CouplingCriterion semanticProximity = context.getCriteriaCatalog().getCriterionByName(CouplingCriterion.SEMANTIC_PROXIMITY);
        for (EntityRelation relation : entityRelationDiagram.getRelations()) {
            if (EntityRelation.RelationType.AGGREGATION.equals(relation.getType())) {
                CouplingInstance instance = new CouplingInstance(semanticProximity, InstanceType.AGGREGATION);
                List<Nanoentity> originNanoentities = relation.getOrigin().getNanoentities().stream()
                        .map(attr -> context.findNanoEntityByContextAndName(relation.getOrigin().getName(), attr)).collect(Collectors.toList());
                List<Nanoentity> destinationNanoentities = relation.getDestination().getNanoentities().stream()
                        .map(attr -> context.findNanoEntityByContextAndName(relation.getDestination().getName(), attr)).collect(Collectors.toList());
                instance.setNanoentities(originNanoentities);
                instance.setSecondNanoentities(destinationNanoentities);
                instance.setName(relation.getOrigin().getName() + "." + relation.getDestination().getName());
                context.addCouplingInstance(instance);
            }
        }
    }

    /**
     * Basically the User Representations import in original ServiceCutter
     */
    private void initUserRepresentationRelatedData() {
        // only if user representations are provided
        if (userRepresentationContainer == null)
            return;

        createUseCaseCouplingInstances(userRepresentationContainer.getUseCases());
        Compatibilities compatibilities = userRepresentationContainer.getCompatibilities();
        if (compatibilities != null) {
            createCharacteristicCouplingInstances(compatibilities.getStructuralVolatility(), CouplingCriterion.STRUCTURAL_VOLATILITY);
            createCharacteristicCouplingInstances(compatibilities.getConsistencyCriticality(), CouplingCriterion.CONSISTENCY);
            createCharacteristicCouplingInstances(compatibilities.getSecurityCriticality(), CouplingCriterion.SECURITY_CRITICALITY);
            createCharacteristicCouplingInstances(compatibilities.getStorageSimilarity(), CouplingCriterion.STORAGE_SIMILARITY);
            createCharacteristicCouplingInstances(compatibilities.getContentVolatility(), CouplingCriterion.CONTENT_VOLATILITY);
            createCharacteristicCouplingInstances(compatibilities.getAvailabilityCriticality(), CouplingCriterion.AVAILABILITY);
        }
        createRelatedGroupCouplingInstances(userRepresentationContainer.getAggregates(), CouplingCriterion.CONSISTENCY_CONSTRAINT);
        createRelatedGroupCouplingInstances(userRepresentationContainer.getEntities(), CouplingCriterion.IDENTITY_LIFECYCLE);
        createRelatedGroupCouplingInstances(userRepresentationContainer.getPredefinedServices(), CouplingCriterion.PREDEFINED_SERVICE);
        createRelatedGroupCouplingInstances(userRepresentationContainer.getSecurityAccessGroups(), CouplingCriterion.SECURITY_CONTEXUALITY);
        createRelatedGroupCouplingInstances(userRepresentationContainer.getSeparatedSecurityZones(), CouplingCriterion.SECURITY_CONSTRAINT);
        createRelatedGroupCouplingInstances(userRepresentationContainer.getSharedOwnerGroups(), CouplingCriterion.SHARED_OWNER);
    }

    private void createUseCaseCouplingInstances(final List<UseCase> useCases) {
        CouplingCriterion semanticProximity = context.getCriteriaCatalog().getCriterionByName(CouplingCriterion.SEMANTIC_PROXIMITY);
        for (UseCase usecase : useCases) {
            InstanceType type;
            type = usecase.isLatencyCritical() ? InstanceType.LATENCY_USE_CASE : InstanceType.USE_CASE;
            CouplingInstance instance = new CouplingInstance(semanticProximity, type);
            instance.setName(usecase.getName());
            instance.setNanoentities(findNanoentities(usecase.getNanoentitiesRead()));
            instance.setSecondNanoentities(findNanoentities(usecase.getNanoentitiesWritten()));
            context.addCouplingInstance(instance);
            log.info("Import use cases {} with fields written {} and fields read {}", usecase.getName(), usecase.getNanoentitiesWritten(), usecase.getNanoentitiesRead());
        }
    }

    private void createCharacteristicCouplingInstances(final List<Characteristic> characteristics, final String criterionName) {
        if (characteristics == null || characteristics.isEmpty()) {
            return;
        }
        for (Characteristic inputCharacteristic : characteristics) {
            CouplingCriterionCharacteristic characteristic = findCharacteristic(criterionName, inputCharacteristic.getCharacteristic());
            if (characteristic == null) {
                log.error("characteristic {} not known! ignoring...", inputCharacteristic);
                continue;
            }
            Set<CouplingInstance> instance = context.findCouplingInstancesByCharacteristic(characteristic);

            if (instance == null || instance.isEmpty()) {
                CouplingInstance newInstance = new CouplingInstance(characteristic, InstanceType.CHARACTERISTIC);
                newInstance.setName(criterionName);
                newInstance.setNanoentities(findNanoentities(inputCharacteristic.getNanoentities()));
                context.addCouplingInstance(newInstance);
                log.info("Import distance characteristic {}-{} with nanoentities {}", criterionName, inputCharacteristic.getCharacteristic(), newInstance.getAllNanoentities());
            } else {
                log.error("enhancing characteristics not yet implemented. criterion: {}, characteristic: {}", criterionName, inputCharacteristic.getCharacteristic());
            }
        }
        completeSystemWithDefaultsForDistance();
    }

    private void createRelatedGroupCouplingInstances(final List<RelatedGroup> listOfGroups, final String couplingCriterionName) {
        if (listOfGroups == null || listOfGroups.isEmpty()) {
            return;
        }
        CouplingCriterion couplingCriterion = context.getCriteriaCatalog().getCriterionByName(couplingCriterionName);
        for (RelatedGroup relatedGroup : listOfGroups) {
            CouplingInstance instance = new CouplingInstance(couplingCriterion, InstanceType.RELATED_GROUP);
            String name = relatedGroup.getName();
            instance.setName((name != null && !"".equals(name)) ? name : couplingCriterionName);
            instance.setNanoentities(findNanoentities(relatedGroup.getNanoentities()));
            context.addCouplingInstance(instance);
            log.info("Import related group {} on nanoentities {} ", name, instance.getNanoentities());
        }
    }

    private CouplingCriterionCharacteristic findCharacteristic(final String coupling, final String characteristic) {
        CouplingCriterion couplingCriterion = context.getCriteriaCatalog().getCriterionByName(coupling);
        CouplingCriterionCharacteristic result = context.getCriteriaCatalog().getCouplingCriterionCharacteristicByNameAndCouplingCriterion(characteristic, couplingCriterion);
        return result;
    }

    private List<Nanoentity> findNanoentities(final List<String> names) {
        List<Nanoentity> nanoentities = new ArrayList<>();
        for (String nanoentityName : names) {
            Nanoentity nanoentity;
            if (nanoentityName.contains(".")) {
                String[] splittedName = nanoentityName.split("\\.");
                nanoentity = context.findNanoEntityByContextAndName(splittedName[0], splittedName[1]);
            } else {
                nanoentity = context.findNanoEntityByName(nanoentityName);
            }

            if (nanoentity != null) {
                nanoentities.add(nanoentity);
            } else {
                log.warn("nanoentity with name {} not known!", nanoentityName);
            }
        }
        return nanoentities;
    }

    private Nanoentity createNanoentity(final String context, final String name) {
        Nanoentity nanoentity = new Nanoentity();
        nanoentity.setId(nanoEntityId++);
        nanoentity.setName(name);
        nanoentity.setContext(context);
        this.context.addNanoEntity(nanoentity);
        return nanoentity;
    }

    private List<TemporaryNanoentity> expandEntitiesByCompositionAndInheritance(final EntityRelationDiagram erd, List<TemporaryNanoentity> nanoentities) {
        List<EntityRelation> currentRelations = new ArrayList<>(erd.getRelations());

        // does this work with cycles?
        List<EntityRelation> relationsToEdgeEntities = getRelationsToEdgeEntities(currentRelations, erd.getEntities());
        while (!relationsToEdgeEntities.isEmpty()) {
            for (EntityRelation relation : relationsToEdgeEntities) {
                String destinationEntity = relation.getDestination().getName();
                String originEntity = relation.getOrigin().getName();
                if (EntityRelation.RelationType.COMPOSITION.equals(relation.getType())) {
                    // move all nanoentities from the destination to the origin
                    // entity
                    nanoentities.stream().filter(n -> destinationEntity.equals(n.getOriginalEntity())).forEach(n -> n.setNewEntity(originEntity));
                } else if (EntityRelation.RelationType.INHERITANCE.equals(relation.getType())) {
                    // move all nanoentities from the origin to the destination
                    // entity
                    nanoentities.stream().filter(n -> originEntity.equals(n.getOriginalEntity())).forEach(n -> n.setNewEntity(destinationEntity));
                }

                currentRelations.remove(relation);
                relationsToEdgeEntities = getRelationsToEdgeEntities(currentRelations, erd.getEntities());
            }
        }
        return nanoentities;
    }

    private List<EntityRelation> getRelationsToEdgeEntities(final List<EntityRelation> currentRelations, final List<Entity> inputEntites) {
        // get all entites that will have no other entities merged into them
        List<Entity> reducableEntities = inputEntites.stream()
                .filter(entity -> currentRelations.stream().filter(
                        r2 -> (r2.getDestination().equals(entity) && r2.getType().equals(EntityRelation.RelationType.INHERITANCE)) || (r2.getOrigin().equals(entity) && r2.getType().equals(EntityRelation.RelationType.COMPOSITION)))
                        .collect(Collectors.toList()).isEmpty()).collect(Collectors.toList());

        // get all relations that will merge the reducableEntities into
        // another entity
        List<EntityRelation> relationsToEdgeEntities = currentRelations.stream().filter(r -> (reducableEntities.contains(r.getOrigin()) && r.getType().equals(EntityRelation.RelationType.INHERITANCE))
                || (reducableEntities.contains(r.getDestination()) && r.getType().equals(EntityRelation.RelationType.COMPOSITION))).collect(Collectors.toList());
        return relationsToEdgeEntities;
    }

    public void completeSystemWithDefaultsForDistance() {
        Set<Nanoentity> allNanoentitiesInModel = context.getNanoEntities();
        Map<String, Set<CouplingInstance>> instancesByCriterion = context.findCouplingInstancesGroupedByCriterionFilteredByCriterionType(CouplingType.COMPATIBILITY);

        // For every criterion
        for (Map.Entry<String, Set<CouplingInstance>> criterion : instancesByCriterion.entrySet()) {
            Set<Nanoentity> definedNanoentities = criterion.getValue().stream().flatMap(instance -> instance.getAllNanoentities().stream()).collect(Collectors.toSet());
            // find missing nanoentities which need to have an instance
            Set<Nanoentity> missingNanoentities = allNanoentitiesInModel.stream().filter(nanoentity -> !definedNanoentities.contains(nanoentity)).collect(Collectors.toSet());

            if (!missingNanoentities.isEmpty()) {
                CouplingCriterionCharacteristic defaultCharacteristic = context.getCriteriaCatalog()
                        .getCouplingCriterionCharacteristicByCouplingCriterionAndIsDefault(context.getCriteriaCatalog().getCriterionByName(criterion.getKey()));
                Set<CouplingInstance> instances = context.findCouplingInstancesByCharacteristic(defaultCharacteristic);
                CouplingInstance instance;
                if (instances.size() == 1) {
                    instance = instances.iterator().next();
                } else if (instances.size() == 0) {
                    instance = new CouplingInstance(defaultCharacteristic, InstanceType.CHARACTERISTIC);
                    instance.setName(defaultCharacteristic.getName());
                    context.addCouplingInstance(instance);
                } else {
                    throw new RuntimeException("only one instance per characteristic expected for distance criterion");
                }
                for (Nanoentity nanoentity : missingNanoentities) {
                    instance.addNanoentity(nanoentity);
                }
                log.info("Complete model with instance of characteristic {} of criterion {} and nanoentities {}", defaultCharacteristic.getName(), criterion.getKey(),
                        missingNanoentities);
            }

        }
    }

    private class TemporaryNanoentity {

        private String originalEntity;
        private String originalName;
        private String newEntity;

        public TemporaryNanoentity(String originalEntity, String originalName) {
            this.originalEntity = originalEntity;
            this.originalName = originalName;
            this.newEntity = originalEntity;
        }

        public String getOriginalEntity() {
            return originalEntity;
        }

        public String getOriginalName() {
            return originalName;
        }

        public String getNewEntity() {
            return newEntity;
        }

        public void setNewEntity(String newEntity) {
            this.newEntity = newEntity;
        }

    }

}
