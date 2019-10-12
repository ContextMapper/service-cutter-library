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

import ch.hsr.servicecutter.analyzer.ServiceCutAnalyzer;
import ch.hsr.servicecutter.api.model.SolverResult;
import ch.hsr.servicecutter.model.solver.EntityPair;
import ch.hsr.servicecutter.model.usersystem.UserSystem;
import ch.hsr.servicecutter.scorer.Score;
import ch.hsr.servicecutter.scorer.Scorer;
import ch.hsr.servicecutter.solver.GraphStreamSolver;
import ch.hsr.servicecutter.solver.Solver;
import ch.hsr.servicecutter.solver.SolverAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

import static ch.hsr.servicecutter.solver.SolverAlgorithm.LEUNG;

/**
 * Main class to run generate service decompositions. Runs the internal solver and returns the cutting result.
 *
 * @author Stefan Kapferer
 */
public class ServiceCutter {

    private final Logger log = LoggerFactory.getLogger(ServiceCutter.class);

    private ServiceCutterContext context;
    private Scorer scorer;
    private ServiceCutAnalyzer analyzer;

    /**
     * Needs a {@link ServiceCutterContext} to generate service decompositions. Use {@link ServiceCutterContextBuilder}
     * to build the context.
     */
    public ServiceCutter(ServiceCutterContext context) {
        this.context = context;
        this.scorer = new Scorer(context.getCouplingInstances(), context.getNanoEntities());
        this.analyzer = new ServiceCutAnalyzer(context.getNanoEntities());
    }

    public SolverResult generateDecomposition() {
        UserSystem userSystem = context.getUserSystem();
        if (userSystem == null || context.getSolverConfiguration() == null || context.getSolverConfiguration().getPriorities().isEmpty()) {
            return new SolverResult(Collections.emptySet());
        }
        Solver solver = null;
        SolverAlgorithm algorithm = context.getSolverConfiguration().getAlgorithm();

        Map<EntityPair, Map<String, Score>> scores = scorer.getScores((final String key) -> {
            return context.getSolverConfiguration().getPriorityForCouplingCriterion(key).toValue();
        });
        if (LEUNG.equals(algorithm)) {
            solver = new GraphStreamSolver(userSystem, scores, context.getSolverConfiguration());
        } else {
            throw new RuntimeException("Algorithm " + algorithm.toString() + " not found!");
        }
        log.info("created graph");
        SolverResult result = solver.solve();
        log.info("found clusters");
        log.info("userSystem {} solved, found {} bounded contexts: {}", userSystem.getName(), result.getServices().size(), result.toString());
        if (result.getServices().size() > 0) {
            analyzer.analyseResult(result, scores, userSystem);
        } else {
            log.warn("no services found!");
        }
        return result;
    }

}
