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
package ch.hsr.servicecutter.model.criteria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ch.hsr.servicecutter.model.criteria.CouplingCriterion.*;
import static ch.hsr.servicecutter.model.criteria.CouplingType.*;

/**
 * Class initializes and provides access to all CouplingCriterion instances.
 *
 * @author Stefan Kapferer
 */
public class CouplingCriteriaCatalog {

    private Map<String, CouplingCriterion> couplingCriteriaCatalog;
    private List<CouplingCriterionCharacteristic> couplingCriteriaCharacteristics;

    public CouplingCriteriaCatalog() {
        this.couplingCriteriaCatalog = new HashMap<>();
        this.couplingCriteriaCharacteristics = new ArrayList<>();
        initializeCatalog();
    }

    public CouplingCriterion getCriterionByName(String name) {
        return this.couplingCriteriaCatalog.get(name);
    }

    public List<CouplingCriterionCharacteristic> getCouplingCriteriaCharacteristics4CriterionName(String criterionName) {
        return new ArrayList<>(this.couplingCriteriaCharacteristics.stream().filter(cc -> cc.getCouplingCriterion().getName().equals(criterionName)).collect(Collectors.toList()));
    }

    public CouplingCriterionCharacteristic getCouplingCriterionCharacteristicByNameAndCouplingCriterion(String name, CouplingCriterion couplingCriterion) {
        return this.couplingCriteriaCharacteristics.stream().filter(ccc -> ccc.getName().equals(name) && ccc.getCouplingCriterion().equals(couplingCriterion)).findFirst().get();
    }

    public CouplingCriterionCharacteristic getCouplingCriterionCharacteristicByCouplingCriterionAndIsDefault(CouplingCriterion couplingCriterion) {
        return this.couplingCriteriaCharacteristics.stream().filter(ccc -> ccc.getCouplingCriterion().equals(couplingCriterion) && ccc.isDefault()).findFirst().get();
    }

    private void initializeCatalog() {
        this.couplingCriteriaCatalog.put(IDENTITY_LIFECYCLE, new CouplingCriterion(IDENTITY_LIFECYCLE, "Nanoentities that belong to the same identity and therefore share a common lifecycle.", COHESIVENESS));
        this.couplingCriteriaCatalog.put(SEMANTIC_PROXIMITY, new CouplingCriterion(SEMANTIC_PROXIMITY, "Two nanoentities are semantically proximate when they have a semantic connection given by the business domain. The strongest indicator for semantic proximity is coherent access on nanoentities within the same use case.", COHESIVENESS));
        this.couplingCriteriaCatalog.put(SHARED_OWNER, new CouplingCriterion(SHARED_OWNER, "The same person, role or department is responsible for a group of nanoentities. Service decomposition should try to keep entities with the same responsible role together while not mixing entities with different responsible instances in one service.", COHESIVENESS));
        this.couplingCriteriaCatalog.put(STRUCTURAL_VOLATILITY, new CouplingCriterion(STRUCTURAL_VOLATILITY, "How often change requests need to be implemented affecting nanoentities.", COMPATIBILITY));
        this.couplingCriteriaCatalog.put(LATENCY, new CouplingCriterion(LATENCY, "Groups of nanoentities with high performance requirements for a specific user request. These nanoentities should be modelled in the same service to avoid remote calls.", COHESIVENESS));
        this.couplingCriteriaCatalog.put(CONSISTENCY, new CouplingCriterion(CONSISTENCY, "Some data such as financial records loses its value in case of inconsistencies while other data is more tolerant to inconsistencies.", COMPATIBILITY));
        this.couplingCriteriaCatalog.put(AVAILABILITY, new CouplingCriterion(AVAILABILITY, "Nanoentities have varying availability constraints. Some are critical while others can be unavailable for some time. As providing high availability comes at a cost, nanoentities classified with different characteristics should not be composed in the same service.", COMPATIBILITY));
        this.couplingCriteriaCatalog.put(CONTENT_VOLATILITY, new CouplingCriterion(CONTENT_VOLATILITY, "A nanoentity can be classified by its volatility which defines how frequent it is updated. Highly volatile and more stable nanoentities should be composed in different services.", COMPATIBILITY));
        this.couplingCriteriaCatalog.put(CONSISTENCY_CONSTRAINT, new CouplingCriterion(CONSISTENCY_CONSTRAINT, "A group of nanoentities that have a dependent state and therefore need to be kept consistent to each other.", CONSTRAINTS));
        this.couplingCriteriaCatalog.put(STORAGE_SIMILARITY, new CouplingCriterion(STORAGE_SIMILARITY, "Storage that is required to persist all instances of a nanoentity.", COMPATIBILITY));
        this.couplingCriteriaCatalog.put(PREDEFINED_SERVICE, new CouplingCriterion(PREDEFINED_SERVICE, "There might be the following reasons why some nanoentities forcefully need to be modelled in the same service: Technological optimizations or Legacy systems", CONSTRAINTS));
        this.couplingCriteriaCatalog.put(SECURITY_CONTEXUALITY, new CouplingCriterion(SECURITY_CONTEXUALITY, "A security role is allowed to see or process a group of nanoentities. Mixing security contexts in one service complicates authentication and authorization implementations.", COHESIVENESS));
        this.couplingCriteriaCatalog.put(SECURITY_CRITICALITY, new CouplingCriterion(SECURITY_CRITICALITY, "Criticality of an nanoentity in case of data loss or a privacy violation. Represents the reputational or financial damage when the information is disclosed to unauthorized parties. As high security criticality comes at a cost, nanoentities classified with different characteristics should not be composed in the same service.", COMPATIBILITY));
        this.couplingCriteriaCatalog.put(SECURITY_CONSTRAINT, new CouplingCriterion(SECURITY_CONSTRAINT, "Groups of nanoentities are semantically related but must not reside in the same service in order to satisfy information security requirements. This restriction can be established by an external party such as a certification authority or an internal design team.", CONSTRAINTS));

        this.couplingCriteriaCharacteristics.add(new CouplingCriterionCharacteristic(couplingCriteriaCatalog.get(STRUCTURAL_VOLATILITY), "Often", 10, false));
        this.couplingCriteriaCharacteristics.add(new CouplingCriterionCharacteristic(couplingCriteriaCatalog.get(STRUCTURAL_VOLATILITY), "Normal", 4, true));
        this.couplingCriteriaCharacteristics.add(new CouplingCriterionCharacteristic(couplingCriteriaCatalog.get(STRUCTURAL_VOLATILITY), "Rarely", 0, false));

        this.couplingCriteriaCharacteristics.add(new CouplingCriterionCharacteristic(couplingCriteriaCatalog.get(CONSISTENCY), "High", 10, true));
        this.couplingCriteriaCharacteristics.add(new CouplingCriterionCharacteristic(couplingCriteriaCatalog.get(CONSISTENCY), "Eventually", 4, false));
        this.couplingCriteriaCharacteristics.add(new CouplingCriterionCharacteristic(couplingCriteriaCatalog.get(CONSISTENCY), "Weak", 0, false));

        this.couplingCriteriaCharacteristics.add(new CouplingCriterionCharacteristic(couplingCriteriaCatalog.get(AVAILABILITY), "Critical", 10, false));
        this.couplingCriteriaCharacteristics.add(new CouplingCriterionCharacteristic(couplingCriteriaCatalog.get(AVAILABILITY), "Normal", 4, true));
        this.couplingCriteriaCharacteristics.add(new CouplingCriterionCharacteristic(couplingCriteriaCatalog.get(AVAILABILITY), "Low", 0, false));

        this.couplingCriteriaCharacteristics.add(new CouplingCriterionCharacteristic(couplingCriteriaCatalog.get(CONTENT_VOLATILITY), "Often", 10, false));
        this.couplingCriteriaCharacteristics.add(new CouplingCriterionCharacteristic(couplingCriteriaCatalog.get(CONTENT_VOLATILITY), "Regularly", 5, true));
        this.couplingCriteriaCharacteristics.add(new CouplingCriterionCharacteristic(couplingCriteriaCatalog.get(CONTENT_VOLATILITY), "Rarely", 0, false));

        this.couplingCriteriaCharacteristics.add(new CouplingCriterionCharacteristic(couplingCriteriaCatalog.get(STORAGE_SIMILARITY), "Tiny", 0, false));
        this.couplingCriteriaCharacteristics.add(new CouplingCriterionCharacteristic(couplingCriteriaCatalog.get(STORAGE_SIMILARITY), "Normal", 3, true));
        this.couplingCriteriaCharacteristics.add(new CouplingCriterionCharacteristic(couplingCriteriaCatalog.get(STORAGE_SIMILARITY), "Huge", 10, false));

        this.couplingCriteriaCharacteristics.add(new CouplingCriterionCharacteristic(couplingCriteriaCatalog.get(SECURITY_CRITICALITY), "Critical", 10, false));
        this.couplingCriteriaCharacteristics.add(new CouplingCriterionCharacteristic(couplingCriteriaCatalog.get(SECURITY_CRITICALITY), "Internal", 3, true));
        this.couplingCriteriaCharacteristics.add(new CouplingCriterionCharacteristic(couplingCriteriaCatalog.get(SECURITY_CRITICALITY), "Public", 0, false));
    }

}
