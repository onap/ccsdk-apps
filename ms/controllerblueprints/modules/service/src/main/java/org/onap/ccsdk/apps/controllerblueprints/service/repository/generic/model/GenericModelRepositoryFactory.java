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

import javax.persistence.EntityManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;

public class GenericModelRepositoryFactory extends JpaRepositoryFactory {

    /**
     * Creates a new {@link JpaRepositoryFactory}.
     *
     * @param entityManager must not be {@literal null}
     */
    public GenericModelRepositoryFactory(EntityManager entityManager) {
        super(entityManager);
    }

    @NotNull
    @Override
    protected SimpleJpaRepository<?, ?> getTargetRepository(RepositoryInformation information,
        @NotNull EntityManager entityManager) {
        return new GenericModelRepositoryImpl(getEntityInformation(information.getDomainType()), entityManager);
    }

    @NotNull
    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        return GenericModelRepositoryImpl.class;
    }
}