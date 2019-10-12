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
package ch.hsr.servicecutter.model;

import ch.hsr.servicecutter.model.criteria.CouplingCriteriaCatalog;
import ch.hsr.servicecutter.model.criteria.CouplingCriterion;
import org.junit.jupiter.api.Test;

import static ch.hsr.servicecutter.model.criteria.CouplingCriterion.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CouplingCriteriaCatalogTest {

    @Test
    public void containsAllCriteria() {
        // given
        CouplingCriteriaCatalog catalog = new CouplingCriteriaCatalog();

        // then
        assertEquals(IDENTITY_LIFECYCLE, catalog.getCriterionByName(IDENTITY_LIFECYCLE).getName());
        assertEquals(SEMANTIC_PROXIMITY, catalog.getCriterionByName(SEMANTIC_PROXIMITY).getName());
        assertEquals(SHARED_OWNER, catalog.getCriterionByName(SHARED_OWNER).getName());
        assertEquals(STRUCTURAL_VOLATILITY, catalog.getCriterionByName(STRUCTURAL_VOLATILITY).getName());
        assertEquals(LATENCY, catalog.getCriterionByName(LATENCY).getName());
        assertEquals(CONSISTENCY, catalog.getCriterionByName(CONSISTENCY).getName());
        assertEquals(AVAILABILITY, catalog.getCriterionByName(AVAILABILITY).getName());
        assertEquals(CONTENT_VOLATILITY, catalog.getCriterionByName(CONTENT_VOLATILITY).getName());
        assertEquals(CONSISTENCY_CONSTRAINT, catalog.getCriterionByName(CONSISTENCY_CONSTRAINT).getName());
        assertEquals(STORAGE_SIMILARITY, catalog.getCriterionByName(STORAGE_SIMILARITY).getName());
        assertEquals(PREDEFINED_SERVICE, catalog.getCriterionByName(PREDEFINED_SERVICE).getName());
        assertEquals(SECURITY_CONTEXUALITY, catalog.getCriterionByName(SECURITY_CONTEXUALITY).getName());
        assertEquals(SECURITY_CRITICALITY, catalog.getCriterionByName(SECURITY_CRITICALITY).getName());
        assertEquals(SECURITY_CONSTRAINT, catalog.getCriterionByName(SECURITY_CONSTRAINT).getName());
    }

    @Test
    public void canGetCharacteristics4CriterionName() {
        // given
        CouplingCriteriaCatalog catalog = new CouplingCriteriaCatalog();

        // then
        assertEquals(3, catalog.getCouplingCriteriaCharacteristics4CriterionName(STRUCTURAL_VOLATILITY).size());
        assertEquals(3, catalog.getCouplingCriteriaCharacteristics4CriterionName(CONSISTENCY).size());
        assertEquals(3, catalog.getCouplingCriteriaCharacteristics4CriterionName(AVAILABILITY).size());
        assertEquals(3, catalog.getCouplingCriteriaCharacteristics4CriterionName(CONTENT_VOLATILITY).size());
        assertEquals(3, catalog.getCouplingCriteriaCharacteristics4CriterionName(STORAGE_SIMILARITY).size());
        assertEquals(3, catalog.getCouplingCriteriaCharacteristics4CriterionName(SECURITY_CRITICALITY).size());
    }

}
