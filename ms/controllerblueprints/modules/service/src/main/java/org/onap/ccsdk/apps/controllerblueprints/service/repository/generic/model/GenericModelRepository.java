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
package org.onap.ccsdk.apps.controllerblueprints.service.repository.generic.model;

import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GenericModelRepository<T> extends JpaRepository<T, String> {

    /**
     * This is a findById method
     *
     * @param id id
     * @return Optional<T>
     */
    @NotNull
    Optional<T> findById(@NotNull String id);

    /**
     * This is a findByArtifactNameAndArtifactVersion method
     *
     * @param artifactName artifactName
     * @param artifactVersion artifactVersion
     * @return Optional<T>
     */
    Optional<T> findByArtifactNameAndArtifactVersion(String artifactName, String artifactVersion);

    /**
     * This is a findTopByArtifactNameOrderByArtifactIdDesc method
     *
     * @param artifactName artifactName
     * @return Optional<T>
     */
    Optional<T> findTopByArtifactNameOrderByArtifactVersionDesc(String artifactName);

    /**
     * This is a findTopByArtifactName method
     *
     * @param artifactName artifactName
     * @return Optional<T>
     */
    @SuppressWarnings("unused")
    List<T> findTopByArtifactName(String artifactName);

    /**
     * This is a findByTagsContainingIgnoreCase method
     *
     * @param tags tags
     * @return Optional<ModelType>
     */
    List<T> findByTagsContainingIgnoreCase(String tags);

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
