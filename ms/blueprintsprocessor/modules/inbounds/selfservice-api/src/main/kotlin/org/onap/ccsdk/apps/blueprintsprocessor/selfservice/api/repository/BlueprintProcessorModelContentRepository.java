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

package org.onap.ccsdk.apps.blueprintsprocessor.selfservice.api.repository;

import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.onap.ccsdk.apps.blueprintsprocessor.selfservice.api.domain.BlueprintProcessorModelContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * BlueprintProcessorModelContentRepository.java Purpose: Provide BlueprintProcessorModelContentRepository of Repository
 *
 * @author Brinda Santh
 * @version 1.0
 */
@Repository
public interface BlueprintProcessorModelContentRepository extends
    JpaRepository<BlueprintProcessorModelContent, String> {

    /**
     * This is a findById method
     *
     * @param id id
     * @return Optional<BlueprintProcessorModelContent>
     */
    @NotNull
    Optional<BlueprintProcessorModelContent> findById(@NotNull String id);

    /**
     * This is a findTopByBlueprintModelAndContentType method
     *
     * @param blueprintModel blueprintModel
     * @param contentType contentType
     * @return Optional<BlueprintProcessorModelContent>
     */
    @SuppressWarnings("unused")
    Optional<BlueprintProcessorModelContent> findTopByBlueprintModelAndContentType(
        BlueprintProcessorModelContent blueprintModel,
        String contentType);

    /**
     * This is a findByBlueprintModelAndContentType method
     *
     * @param blueprintModel blueprintModel
     * @param contentType contentType
     * @return Optional<BlueprintProcessorModelContent>
     */
    @SuppressWarnings("unused")
    List<BlueprintProcessorModelContent> findByBlueprintModelAndContentType(
        BlueprintProcessorModelContent blueprintModel, String contentType);

    /**
     * This is a findByBlueprintModel method
     *
     * @param blueprintModel blueprintModel
     * @return Optional<BlueprintProcessorModelContent>
     */
    @SuppressWarnings("unused")
    List<BlueprintProcessorModelContent> findByBlueprintModel(BlueprintProcessorModelContent blueprintModel);

    /**
     * This is a findByBlueprintModelAndContentTypeAndName method
     *
     * @param blueprintModel blueprintModel
     * @param contentType contentType
     * @param name name
     * @return Optional<BlueprintProcessorModelContent>
     */
    @SuppressWarnings("unused")
    Optional<BlueprintProcessorModelContent> findByBlueprintModelAndContentTypeAndName(
        BlueprintProcessorModelContent blueprintModel,
        String contentType, String name);

    /**
     * This is a deleteByMdeleteByBlueprintModelodelName method
     *
     * @param blueprintModel blueprintModel
     */
    void deleteByBlueprintModel(BlueprintProcessorModelContent blueprintModel);

    /**
     * This is a deleteById method
     *
     * @param id id
     */
    void deleteById(@NotNull String id);

}