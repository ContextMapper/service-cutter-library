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

import ch.hsr.servicecutter.api.model.SolverResult;
import ch.hsr.servicecutter.model.criteria.CouplingCriterion;
import ch.hsr.servicecutter.solver.SolverConfiguration;
import ch.hsr.servicecutter.solver.SolverPriority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServiceCutterTest {

    @BeforeEach
    void prepare() {
        if (!new File("./src-gen").exists())
            new File("./src-gen").mkdir();
    }

    @Test
    public void canCutBookingSystemWithLeung() throws IOException {
        // given
        EntityRelationDiagramImporterJSON erdImporterJSON = new EntityRelationDiagramImporterJSON();
        UserRepresentationContainerImporterJSON urImporterJSON = new UserRepresentationContainerImporterJSON();
        ServiceCutterContext context = new ServiceCutterContextBuilder(erdImporterJSON.createERDFromJSONFile(new File("./src/test/resources/booking_1_model.json")))
                .withUserRepresentations(urImporterJSON.createUserRepresentationContainerFromJSONFile(new File("./src/test/resources/booking_2_user_representations.json"))).build();
        ServiceCutter serviceCutter = new ServiceCutter(context);

        // when
        SolverResult result = serviceCutter.generateDecomposition();

        // then
        assertTrue(result.getServices().size() > 0);
    }

    @Test
    public void canCutDDDSampleWithLeung() throws IOException {
        // given
        EntityRelationDiagramImporterJSON erdImporterJSON = new EntityRelationDiagramImporterJSON();
        UserRepresentationContainerImporterJSON urImporterJSON = new UserRepresentationContainerImporterJSON();
        ServiceCutterContext context = new ServiceCutterContextBuilder(erdImporterJSON.createERDFromJSONFile(new File("./src/test/resources/ddd_1_model.json")))
                .withUserRepresentations(urImporterJSON.createUserRepresentationContainerFromJSONFile(new File("./src/test/resources/ddd_2_user_representations.json")))
                .build();
        ServiceCutter serviceCutter = new ServiceCutter(context);

        // when
        SolverResult result = serviceCutter.generateDecomposition();

        // then
        assertTrue(result.getServices().size() > 0);
    }

    @Test
    public void canCutTradingSystemWithLeung() throws IOException {
        // given
        EntityRelationDiagramImporterJSON erdImporterJSON = new EntityRelationDiagramImporterJSON();
        UserRepresentationContainerImporterJSON urImporterJSON = new UserRepresentationContainerImporterJSON();
        ServiceCutterContext context = new ServiceCutterContextBuilder(erdImporterJSON.createERDFromJSONFile(new File("./src/test/resources/trading_1_model.json")))
                .withUserRepresentations(urImporterJSON.createUserRepresentationContainerFromJSONFile(new File("./src/test/resources/trading_2_user_representations.json")))
                .build();
        ServiceCutter serviceCutter = new ServiceCutter(context);

        // when
        SolverResult result = serviceCutter.generateDecomposition();

        // then
        assertTrue(result.getServices().size() > 0);
    }

    @Test
    public void canChangeSolverConfiguration() throws IOException {
        // given
        EntityRelationDiagramImporterJSON erdImporterJSON = new EntityRelationDiagramImporterJSON();
        UserRepresentationContainerImporterJSON urImporterJSON = new UserRepresentationContainerImporterJSON();
        SolverConfiguration configuration = new SolverConfigurationFactory().createDefaultConfiguration();
        configuration.setPriority(CouplingCriterion.PREDEFINED_SERVICE, SolverPriority.XS);
        ServiceCutterContext context = new ServiceCutterContextBuilder(erdImporterJSON.createERDFromJSONFile(new File("./src/test/resources/booking_1_model.json")))
                .withUserRepresentations(urImporterJSON.createUserRepresentationContainerFromJSONFile(new File("./src/test/resources/booking_2_user_representations.json")))
                .withCustomSolverConfiguration(configuration)
                .build();
        ServiceCutter serviceCutter = new ServiceCutter(context);

        // when
        SolverResult result = serviceCutter.generateDecomposition();

        // then
        assertTrue(result.getServices().size() > 0);
    }

    @Test
    public void createEmptyResultForConfigurationWithoutPriorities() throws IOException {
        // given
        EntityRelationDiagramImporterJSON erdImporterJSON = new EntityRelationDiagramImporterJSON();
        UserRepresentationContainerImporterJSON urImporterJSON = new UserRepresentationContainerImporterJSON();
        SolverConfiguration configuration = new SolverConfigurationFactory().createDefaultConfiguration();
        configuration.getPriorities().clear();
        ServiceCutterContext context = new ServiceCutterContextBuilder(erdImporterJSON.createERDFromJSONFile(new File("./src/test/resources/booking_1_model.json")))
                .withUserRepresentations(urImporterJSON.createUserRepresentationContainerFromJSONFile(new File("./src/test/resources/booking_2_user_representations.json")))
                .withCustomSolverConfiguration(configuration)
                .build();
        ServiceCutter serviceCutter = new ServiceCutter(context);

        // when
        SolverResult result = serviceCutter.generateDecomposition();

        // then
        assertEquals(0, result.getServices().size());
    }

    @Test
    public void producesResultWhichCanBeSerialized() throws IOException {
        // given
        String resultFile = "./src-gen/booking.json";
        deleteFileIfItExists(resultFile);
        EntityRelationDiagramImporterJSON erdImporterJSON = new EntityRelationDiagramImporterJSON();
        UserRepresentationContainerImporterJSON urImporterJSON = new UserRepresentationContainerImporterJSON();
        ServiceCutterContext context = new ServiceCutterContextBuilder(erdImporterJSON.createERDFromJSONFile(new File("./src/test/resources/booking_1_model.json")))
                .withUserRepresentations(urImporterJSON.createUserRepresentationContainerFromJSONFile(new File("./src/test/resources/booking_2_user_representations.json"))).build();
        ServiceCutter serviceCutter = new ServiceCutter(context);

        // when
        SolverResult result = serviceCutter.generateDecomposition();
        ResultSerializer serializer = new ResultSerializer();
        serializer.serializeResult(result, new File(resultFile));

        // then
        assertTrue(result.getServices().size() > 0);
        assertTrue(new File(resultFile).exists());
    }

    private void deleteFileIfItExists(String path) {
        if (new File(path).exists())
            new File(path).delete();
    }

}
