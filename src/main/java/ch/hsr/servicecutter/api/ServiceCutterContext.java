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

import ch.hsr.servicecutter.model.criteria.CouplingCriteriaCatalog;
import ch.hsr.servicecutter.model.criteria.CouplingCriterionCharacteristic;
import ch.hsr.servicecutter.model.criteria.CouplingType;
import ch.hsr.servicecutter.model.usersystem.CouplingInstance;
import ch.hsr.servicecutter.model.usersystem.Nanoentity;
import ch.hsr.servicecutter.solver.SolverConfiguration;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Context needed to call ServiceCutter solver.
 *
 * @author Stefan Kapferer
 */
public class ServiceCutterContext {

    private String systemName;
    private SolverConfiguration solverConfiguration;
    private CouplingCriteriaCatalog criteriaCatalog;
    private List<CouplingInstance> couplingInstances;
    private Set<Nanoentity> nanoEntities;

    public ServiceCutterContext(String systemName) {
        this.systemName = systemName;
        this.criteriaCatalog = new CouplingCriteriaCatalog();
        this.couplingInstances = new ArrayList<>();
        this.nanoEntities = new HashSet<>();
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSolverConfiguration(SolverConfiguration solverConfiguration) {
        this.solverConfiguration = solverConfiguration;
    }

    public SolverConfiguration getSolverConfiguration() {
        return solverConfiguration;
    }

    public CouplingCriteriaCatalog getCriteriaCatalog() {
        return criteriaCatalog;
    }

    public void addCouplingInstance(CouplingInstance couplingInstance) {
        this.couplingInstances.add(couplingInstance);
    }

    public List<CouplingInstance> getCouplingInstances() {
        return couplingInstances;
    }

    public void addNanoEntity(Nanoentity nanoentity) {
        this.nanoEntities.add(nanoentity);
    }

    public Set<Nanoentity> getNanoEntities() {
        return Collections.unmodifiableSet(nanoEntities);
    }

    public Optional<Nanoentity> findNanoEntityByContextAndName(String context, String name) {
        return this.nanoEntities.stream().filter(ne -> ne.getContext().equals(context) && ne.getName().equals(name)).findFirst();
    }

    public Optional<Nanoentity> findNanoEntityByName(String name) {
        return this.nanoEntities.stream().filter(ne -> ne.getName().equals(name)).findFirst();
    }

    public Set<CouplingInstance> findCouplingInstancesByCharacteristic(CouplingCriterionCharacteristic characteristic) {
        return this.couplingInstances.stream().filter(ci -> ci.getCharacteristic() != null && ci.getCharacteristic().equals(characteristic)).collect(Collectors.toSet());
    }

    public Map<String, Set<CouplingInstance>> findCouplingInstancesGroupedByCriterionFilteredByCriterionType(CouplingType couplingType) {
        return groupByCriterion(getCouplingInstances().stream().filter(instance -> couplingType.equals(instance.getCouplingCriterion().getType())).collect(Collectors.toSet()));
    }

    private Map<String, Set<CouplingInstance>> groupByCriterion(final Set<CouplingInstance> instances) {
        Map<String, Set<CouplingInstance>> instancesByCriterion = new HashMap<>();
        for (CouplingInstance instance : instances) {
            String ccName = instance.getCouplingCriterion().getName();
            if (instancesByCriterion.get(ccName) == null) {
                instancesByCriterion.put(ccName, new HashSet<CouplingInstance>());
            }
            instancesByCriterion.get(ccName).add(instance);
        }
        return instancesByCriterion;
    }

}
