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

import ch.hsr.servicecutter.api.exception.InputJSONFileNotExisting;
import ch.hsr.servicecutter.api.model.UserRepresentationContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRepresentationContainerImporterJSONTest {

    @Test
    public void canReadUserRepresentationsContainerModelFromJSONFile() throws IOException {
        // given
        File inputFile = new File("./src/test/resources/booking_2_user_representations.json");
        UserRepresentationContainerImporterJSON importer = new UserRepresentationContainerImporterJSON();

        // when
        UserRepresentationContainer model = importer.createUserRepresentationContainerFromJSONFile(inputFile);

        // then
        assertEquals(5, model.getUseCases().size());
        assertEquals(2, model.getCompatibilities().getAvailabilityCriticality().size());
        assertEquals(1, model.getAggregates().size());
        assertEquals(2, model.getSecurityAccessGroups().size());
        assertEquals(1, model.getPredefinedServices().size());
    }

    @Test
    public void expectExceptionIfFileNotExists() {
        // given
        File inputFile = new File("./src/test/resources/just_some_file_name.json");
        UserRepresentationContainerImporterJSON importer = new UserRepresentationContainerImporterJSON();

        // when, then
        Assertions.assertThrows(InputJSONFileNotExisting.class, () -> {
            importer.createUserRepresentationContainerFromJSONFile(inputFile);
        });
    }

}
