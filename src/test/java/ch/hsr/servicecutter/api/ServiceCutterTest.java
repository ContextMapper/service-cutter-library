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
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServiceCutterTest {

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
                .withUserRepresentations(urImporterJSON.createUserRepresentationContainerFromJSONFile(new File("./src/test/resources/ddd_2_user_representations.json"))).build();
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
                .withUserRepresentations(urImporterJSON.createUserRepresentationContainerFromJSONFile(new File("./src/test/resources/trading_2_user_representations.json"))).build();
        ServiceCutter serviceCutter = new ServiceCutter(context);

        // when
        SolverResult result = serviceCutter.generateDecomposition();

        // then
        assertTrue(result.getServices().size() > 0);
    }

}
