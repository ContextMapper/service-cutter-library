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

import ch.hsr.servicecutter.api.*;
import ch.hsr.servicecutter.api.model.EntityRelationDiagram;
import ch.hsr.servicecutter.api.model.SolverResult;
import ch.hsr.servicecutter.api.model.UserRepresentationContainer;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class LeungIntegrationTest {

    @Test
    public void canClusterModel() throws IOException {
        // given
        File erdFile = new File("./src/test/resources/booking_1_model.json");
        File urFile = new File("./src/test/resources/booking_2_user_representations.json");
        EntityRelationDiagram erd = new EntityRelationDiagramImporterJSON().createERDFromJSONFile(erdFile);
        UserRepresentationContainer userRepresentations = new UserRepresentationContainerImporterJSON()
                .createUserRepresentationContainerFromJSONFile(urFile);

        // when
        SolverConfiguration config = new SolverConfigurationFactory().createDefaultConfiguration();
        config.setAlgorithm(SolverAlgorithm.LEUNG);
        ServiceCutterContext context = new ServiceCutterContextBuilder(erd)
                .withUserRepresentations(userRepresentations)
                .withCustomSolverConfiguration(config)
                .build();
        SolverResult result = new ServiceCutter(context).generateDecomposition();

        // then
        assertNotNull(result);
        assertFalse(result.getServices().isEmpty());
    }

}
