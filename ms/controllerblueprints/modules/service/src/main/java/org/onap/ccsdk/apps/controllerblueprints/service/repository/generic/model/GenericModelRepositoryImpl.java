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
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class GenericModelRepositoryImpl<T> extends SimpleJpaRepository<T, String> implements GenericModelRepository<T> {

    public GenericModelRepositoryImpl(
        JpaEntityInformation<T, ?> entityInformation,
        EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    @Override
    public Optional<T> findByArtifactNameAndArtifactVersion(String artifactName, String artifactVersion) {
        return Optional.empty();
    }

    @Override
    public Optional<T> findTopByArtifactNameOrderByArtifactVersionDesc(String artifactName) {
        return Optional.empty();
    }

    @Override
    public List<T> findTopByArtifactName(String artifactName) {
        return null;
    }

    @Override
    public List<T> findByTagsContainingIgnoreCase(String tags) {
        return null;
    }

    @Override
    public void deleteByArtifactNameAndArtifactVersion(String artifactName, String artifactVersion) {

    }
}
