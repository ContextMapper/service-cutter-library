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

import ch.hsr.servicecutter.model.criteria.CouplingCriterion;
import ch.hsr.servicecutter.solver.SolverAlgorithm;
import ch.hsr.servicecutter.solver.SolverConfiguration;
import ch.hsr.servicecutter.solver.SolverPriority;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

import static ch.hsr.servicecutter.model.criteria.CouplingCriterion.*;
import static ch.hsr.servicecutter.solver.SolverPriority.*;

/**
 * Use this class to create a SolverConfiguration.
 *
 * @author Stefan Kapferer
 */
public class SolverConfigurationFactory {

    public SolverConfiguration createDefaultConfiguration() {
        SolverConfiguration configuration = new SolverConfiguration();
        configuration.setAlgorithm(SolverAlgorithm.LEUNG);

        configuration.setAlgorithmParam("inflation", 2.0);
        configuration.setAlgorithmParam("power", 1.0);
        configuration.setAlgorithmParam("prune", 0.0);
        configuration.setAlgorithmParam("extraClusters", 0.0);
        configuration.setAlgorithmParam("numberOfClusters", 3.0);
        configuration.setAlgorithmParam("leungM", 0.1);
        configuration.setAlgorithmParam("leungDelta", 0.55);

        configuration.setPriority(IDENTITY_LIFECYCLE, M);
        configuration.setPriority(SEMANTIC_PROXIMITY, M);
        configuration.setPriority(SHARED_OWNER, M);
        configuration.setPriority(STRUCTURAL_VOLATILITY, XS);
        configuration.setPriority(IDENTITY_LIFECYCLE, M);
        configuration.setPriority(LATENCY, M);
        configuration.setPriority(CONSISTENCY, XS);
        configuration.setPriority(AVAILABILITY, XS);
        configuration.setPriority(CONTENT_VOLATILITY, XS);
        configuration.setPriority(CONSISTENCY_CONSTRAINT, M);
        configuration.setPriority(STORAGE_SIMILARITY, XS);
        configuration.setPriority(PREDEFINED_SERVICE, M);
        configuration.setPriority(SECURITY_CONTEXUALITY, M);
        configuration.setPriority(SECURITY_CRITICALITY, XS);
        configuration.setPriority(SECURITY_CONSTRAINT, M);

        return configuration;
    }

    public SolverConfiguration createConfigurationWithJSONFile(File jsonFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonFile, SolverConfiguration.class);
    }

}
