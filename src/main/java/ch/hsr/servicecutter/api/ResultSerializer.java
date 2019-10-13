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
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * Can be used to serialize the decomposition result as JSON file.
 *
 * @author Stefan Kapferer
 */
public class ResultSerializer {

    public void serializeResult(SolverResult result, File jsonFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(jsonFile, result);
    }

}
