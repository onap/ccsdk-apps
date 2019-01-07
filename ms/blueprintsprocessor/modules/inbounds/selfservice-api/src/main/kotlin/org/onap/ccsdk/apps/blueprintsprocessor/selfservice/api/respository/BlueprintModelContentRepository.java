/*
 * Copyright (C) 2019 Bell Canada.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onap.ccsdk.apps.blueprintsprocessor.selfservice.api.respository;

import org.onap.ccsdk.apps.blueprintsprocessor.selfservice.api.respository.domain.BlueprintProcessorModel;
import org.onap.ccsdk.apps.blueprintsprocessor.selfservice.api.respository.domain.BlueprintProcessorModelContent;
import org.onap.ccsdk.apps.controllerblueprints.service.domain.BlueprintModel;
import org.onap.ccsdk.apps.controllerblueprints.service.domain.BlueprintModelContent;
import org.onap.ccsdk.apps.controllerblueprints.service.repository.generic.model.content.GenericModelContentRepository;

public interface BlueprintModelContentRepository extends
    GenericModelContentRepository<BlueprintProcessorModelContent, BlueprintProcessorModel> {

}
