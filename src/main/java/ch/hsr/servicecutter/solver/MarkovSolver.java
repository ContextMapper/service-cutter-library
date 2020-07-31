/*
 * Copyright 2020 The Context Mapper Project Team
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
package ch.hsr.servicecutter.solver;

import ch.hsr.servicecutter.api.ServiceCutterContext;
import ch.hsr.servicecutter.model.solver.EntityPair;
import ch.hsr.servicecutter.scorer.Score;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.nlpub.watset.graph.Clustering;
import org.nlpub.watset.graph.MarkovClustering;

import java.util.Map;

public class MarkovSolver extends AbstractWatsetSolver {

    private int expansionOperations;
    private double powerCoefficient;

    public MarkovSolver(ServiceCutterContext context, Map<EntityPair, Map<String, Score>> scores, SolverConfiguration config) {
        super(context, scores);
        this.expansionOperations = config.getValueForAlgorithmParam("mclExpansionOperations", 2.0).intValue();
        this.powerCoefficient = config.getValueForAlgorithmParam("mclPowerCoefficient", 2.0).doubleValue();
    }

    @Override
    protected Clustering<String> getAlgorithm() {
        return new MarkovClustering<String, DefaultWeightedEdge>(graph, expansionOperations, powerCoefficient);
    }
}
