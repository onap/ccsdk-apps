/*-
 * ============LICENSE_START=======================================================
 * ONAP : CCSDK.apps
 * ================================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
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
 * ============LICENSE_END=========================================================
 */

package org.onap.ccsdk.apps.ms.neng.persistence.repository;

import java.util.List;
import org.onap.ccsdk.apps.ms.neng.persistence.entity.GeneratedName;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Repository for the GeneratedName entity.
 */
public interface GeneratedNameRespository extends CrudRepository<GeneratedName, Integer> {
    
    /*
     * Finds an entity by element type, name and the 'isReleased' flag.
     */
    public GeneratedName findByElementTypeAndNameAndIsReleased(String elementType, String name, String isReleased);

    /*
     * Finds entities for a given external system ID.
     */
    public List<GeneratedName> findByExternalId(String externalId);

    /*
     * Finds entities for a given external system ID and element type, ignoring any special characters in names.
     */
    @Query(value = "select * from GENERATED_NAME g where g.external_Id=:externalId and "
                 + "REPLACE(REPLACE(REPLACE(element_type,'NAME',''),'-',''),'_','')=:elementType", 
                 nativeQuery = true)
    public GeneratedName findByExternalIdAndRelaxedElementType(@Param("externalId")String externalId,  
                                                               @Param("elementType")String elementType);

    /*
     * Finds the maximum sequence number used for a given prefix and suffix.
     */
    @Query("select max(sequenceNumber) from GeneratedName where prefix=:prefix "
                    + "and ((suffix is null and :suffix is null) or suffix=:suffix)")
    public String findMaxByPrefixAndSuffix(@Param("prefix") String prefix, @Param("suffix") String suffix);

    /*
     * Finds the maximum sequence number used that is greater than the given sequence number,
     * for a given prefix and suffix. 
     */
    @Query("select max(sequenceNumber) from GeneratedName g where g.sequenceNumber > :seqNum "
                    + "and g.prefix=:prefix and ((:suffix is null and g.suffix is null) or g.suffix=:suffix) "
                    + "and g.isReleased='Y'")
    public Long findNextReleasedSeq(@Param("seqNum") Long seqNum, @Param("prefix") String prefix,
                    @Param("suffix") String suffix);

    /*
     * Finds the entity for given type and name, that is NOT already released. 
     */
    @Query("select g from GeneratedName g where g.elementType=:elementType "
                    + "and g.name=:name and (g.isReleased is null or g.isReleased ='N')")
    public GeneratedName findUnReleased(@Param("elementType") String elementType, @Param("name") String name);


}
