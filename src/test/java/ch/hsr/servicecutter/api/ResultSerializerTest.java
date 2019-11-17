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

import ch.hsr.servicecutter.api.model.Service;
import ch.hsr.servicecutter.api.model.SolverResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResultSerializerTest {

    private static final String TEST_GEN_DIR = "./src-gen";
    private static final String TEST_FILE = TEST_GEN_DIR + "/test-result.json";

    @BeforeEach
    void prepare() {
        File testFile = new File(TEST_FILE);
        if (testFile.exists())
            testFile.delete();
        if (!new File(TEST_GEN_DIR).exists())
            new File(TEST_GEN_DIR).mkdir();
    }

    @Test
    public void canSerializeResultToFile() throws IOException {
        // given
        List<String> nanoentities = new ArrayList<>();
        nanoentities.add("Customer.name");
        nanoentities.add("Customer.address");
        Service service = new Service();
        service.setNanoentities(nanoentities);
        service.setId('A');
        Set<Service> services = new HashSet<>();
        services.add(service);
        SolverResult result = new SolverResult();
        result.setServices(services);

        // when
        ResultSerializer serializer = new ResultSerializer();
        serializer.serializeResult(result, new File(TEST_FILE));

        // then
        assertTrue(new File(TEST_FILE).exists());
    }

}
