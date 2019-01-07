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
package org.onap.ccsdk.apps.controllerblueprints.service.repository.generic.model.content;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class GenericModelContentRepositoryImpl<T, B> extends SimpleJpaRepository<T, String> implements
    GenericModelContentRepository<T, B> {

    public GenericModelContentRepositoryImpl(JpaEntityInformation<T, Serializable> entityInformation,
        EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    @Override
    public Optional<T> findTopByBlueprintModelAndContentType(B blueprintModel, String contentType) {
        return Optional.empty();
    }

    @Override
    public List<T> findByBlueprintModelAndContentType(B blueprintModel, String contentType) {
        return null;
    }

    @Override
    public List<T> findByBlueprintModel(B blueprintModel) {
        return null;
    }

    @Override
    public Optional<T> findByBlueprintModelAndContentTypeAndName(B blueprintModel, String contentType, String name) {
        return Optional.empty();
    }

    @Override
    public void deleteByBlueprintModel(B blueprintModel) {

    }
}