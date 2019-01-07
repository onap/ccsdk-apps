/*
 * Copyright © 2017-2018 AT&T Intellectual Property.
 * Modifications Copyright © 2019 Bell Canada.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.onap.ccsdk.apps.blueprintsprocessor.db

import org.onap.ccsdk.apps.blueprintsprocessor.db.primary.domain.BlueprintProcessorModel
import org.onap.ccsdk.apps.blueprintsprocessor.db.primary.domain.BlueprintProcessorModelContent
import org.onap.ccsdk.apps.blueprintsprocessor.db.primary.repository.BlueprintProcessorModelContentRepository
import org.onap.ccsdk.apps.blueprintsprocessor.db.primary.repository.BlueprintProcessorModelRepository
import org.onap.ccsdk.apps.controllerblueprints.core.config.BluePrintLoadConfiguration
import org.onap.ccsdk.apps.controllerblueprints.core.interfaces.BluePrintValidatorService
import org.onap.ccsdk.apps.controllerblueprints.core.service.BlueprintCatalogServiceImpl
import org.springframework.stereotype.Service

/**
Similar/Duplicate implementation in [org.onap.ccsdk.apps.controllerblueprints.service.load.ControllerBlueprintCatalogServiceImpl]
 */
@Service
class BlueprintProcessorCatalogServiceImpl(bluePrintLoadConfiguration: BluePrintLoadConfiguration,
                                           bluePrintValidatorService: BluePrintValidatorService,
                                           blueprintModelContentRepository: BlueprintProcessorModelContentRepository,
                                           blueprintModelRepository: BlueprintProcessorModelRepository)
    : BlueprintCatalogServiceImpl<BlueprintProcessorModel, BlueprintProcessorModelContent>(BlueprintProcessorModel(),
        BlueprintProcessorModelContent(), bluePrintLoadConfiguration, bluePrintValidatorService,
        blueprintModelContentRepository, blueprintModelRepository)