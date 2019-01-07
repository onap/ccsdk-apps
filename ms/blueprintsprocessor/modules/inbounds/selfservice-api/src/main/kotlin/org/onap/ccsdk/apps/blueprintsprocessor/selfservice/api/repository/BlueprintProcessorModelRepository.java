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
import org.onap.ccsdk.apps.blueprintsprocessor.selfservice.api.domain.BlueprintProcessorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * BlueprintProcessorModelRepository.java Purpose: Provide Configuration Generator BlueprintProcessorModelRepository
 *
 * @author Brinda Santh
 * @version 1.0
 */
@Repository
public interface BlueprintProcessorModelRepository extends JpaRepository<BlueprintProcessorModel, String> {

    /**
     * This is a findById method
     *
     * @param id id
     * @return Optional<BlueprintProcessorModel>
     */
    @NotNull
    Optional<BlueprintProcessorModel> findById(@NotNull String id);

    /**
     * This is a findByArtifactNameAndArtifactVersion method
     *
     * @param artifactName artifactName
     * @param artifactVersion artifactVersion
     * @return Optional<BlueprintProcessorModel>
     */
    Optional<BlueprintProcessorModel> findByArtifactNameAndArtifactVersion(String artifactName, String artifactVersion);

    /**
     * This is a findTopByArtifactNameOrderByArtifactIdDesc method
     *
     * @param artifactName artifactName
     * @return Optional<BlueprintProcessorModel>
     */
    Optional<BlueprintProcessorModel> findTopByArtifactNameOrderByArtifactVersionDesc(String artifactName);

    /**
     * This is a findTopByArtifactName method
     *
     * @param artifactName artifactName
     * @return Optional<BlueprintProcessorModel>
     */
    @SuppressWarnings("unused")
    List<BlueprintProcessorModel> findTopByArtifactName(String artifactName);

    /**
     * This is a findByTagsContainingIgnoreCase method
     *
     * @param tags tags
     * @return Optional<BlueprintProcessorModel>
     */
    List<BlueprintProcessorModel> findByTagsContainingIgnoreCase(String tags);

    /**
     * This is a deleteByArtifactNameAndArtifactVersion method
     *
     * @param artifactName artifactName
     * @param artifactVersion artifactVersion
     */
    @SuppressWarnings("unused")
    void deleteByArtifactNameAndArtifactVersion(String artifactName, String artifactVersion);

    /**
     * This is a deleteById method
     *
     * @param id id
     */
    @SuppressWarnings("unused")
    void deleteById(@NotNull String id);

}
