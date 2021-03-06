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
import org.nlpub.watset.graph.ChineseWhispers;
import org.nlpub.watset.graph.Clustering;
import org.nlpub.watset.graph.NodeWeighting;

import java.util.Map;

public class ChineseWhispersSolver extends AbstractWatsetSolver {

    private final NodeWeighting nodeWeighting;

    public ChineseWhispersSolver(ServiceCutterContext context, Map<EntityPair, Map<String, Score>> scores, SolverConfiguration config) {
        super(context, scores);
        this.nodeWeighting = mapNodeWeightingConfig(config.getValueForAlgorithmParam("cwNodeWeighting", 0.0).intValue());
    }

    @Override
    protected Clustering<String> getAlgorithm() {
        return new ChineseWhispers<String, DefaultWeightedEdge>(graph, nodeWeighting);
    }

    /**
     * Map integer value to nodeWeighting parameter here, because Service Cutter only supports numbers as algorithm parameters ...
     * <p>
     * 0 = top (default)
     * 1 = label
     * 2 = linear
     * 3 = log
     * <p>
     * https://github.com/nlpub/watset-java#chinese-whispers
     */
    private NodeWeighting mapNodeWeightingConfig(int nodeWeighting) {
        switch (nodeWeighting) {
            case 1:
                return NodeWeighting.label();
            case 2:
                return NodeWeighting.linear();
            case 3:
                return NodeWeighting.log();
        }
        return NodeWeighting.top();
    }

}
