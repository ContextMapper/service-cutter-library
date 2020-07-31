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

import ch.hsr.servicecutter.solver.SolverAlgorithm;
import ch.hsr.servicecutter.solver.SolverConfiguration;
import ch.hsr.servicecutter.solver.SolverPriority;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static ch.hsr.servicecutter.model.criteria.CouplingCriterion.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SolverConfigurationFactoryTest {

    @Test
    public void canCreateDefaultSolverConfiguration() {
        // given
        SolverConfigurationFactory factory = new SolverConfigurationFactory();

        // when
        SolverConfiguration configuration = factory.createDefaultConfiguration();

        // then
        assertDefaultConfiguration(configuration);
    }

    @Test
    public void canCreateConfigurationFromJSONFile() throws IOException {
        // given
        SolverConfigurationFactory factory = new SolverConfigurationFactory();
        File configurationFile = new File("./src/test/resources/solver-configuration.json");

        // when
        SolverConfiguration configuration = factory.createConfigurationWithJSONFile(configurationFile);

        // then
        assertDefaultConfiguration(configuration);
    }

    private void assertDefaultConfiguration(SolverConfiguration configuration) {
        assertEquals(SolverAlgorithm.MARKOV_CLUSTERING, configuration.getAlgorithm());

        assertEquals(2, configuration.getAlgorithmParams().get("inflation"));
        assertEquals(1, configuration.getAlgorithmParams().get("power"));
        assertEquals(0, configuration.getAlgorithmParams().get("prune"));
        assertEquals(0, configuration.getAlgorithmParams().get("extraClusters"));
        assertEquals(3, configuration.getAlgorithmParams().get("numberOfClusters"));
        assertEquals(0.1, configuration.getAlgorithmParams().get("leungM"));
        assertEquals(0.55, configuration.getAlgorithmParams().get("leungDelta"));

        assertEquals(SolverPriority.M, configuration.getPriorities().get(IDENTITY_LIFECYCLE));
        assertEquals(SolverPriority.M, configuration.getPriorities().get(SEMANTIC_PROXIMITY));
        assertEquals(SolverPriority.M, configuration.getPriorities().get(SHARED_OWNER));
        assertEquals(SolverPriority.XS, configuration.getPriorities().get(STRUCTURAL_VOLATILITY));
        assertEquals(SolverPriority.M, configuration.getPriorities().get(LATENCY));
        assertEquals(SolverPriority.XS, configuration.getPriorities().get(CONSISTENCY));
        assertEquals(SolverPriority.XS, configuration.getPriorities().get(AVAILABILITY));
        assertEquals(SolverPriority.XS, configuration.getPriorities().get(CONTENT_VOLATILITY));
        assertEquals(SolverPriority.M, configuration.getPriorities().get(CONSISTENCY_CONSTRAINT));
        assertEquals(SolverPriority.XS, configuration.getPriorities().get(STORAGE_SIMILARITY));
        assertEquals(SolverPriority.M, configuration.getPriorities().get(PREDEFINED_SERVICE));
        assertEquals(SolverPriority.M, configuration.getPriorities().get(SECURITY_CONTEXUALITY));
        assertEquals(SolverPriority.XS, configuration.getPriorities().get(SECURITY_CRITICALITY));
        assertEquals(SolverPriority.M, configuration.getPriorities().get(SECURITY_CONSTRAINT));
    }

}
