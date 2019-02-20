/*
 * Copyright © 2017-2018 AT&T Intellectual Property.
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

package org.onap.ccsdk.apps.blueprintsprocessor.db.primary

import com.fasterxml.jackson.databind.JsonNode
import org.onap.ccsdk.apps.blueprintsprocessor.db.AbstractDBLibGenericService
import org.onap.ccsdk.apps.blueprintsprocessor.db.BluePrintDBLibGenericService
import org.onap.ccsdk.apps.blueprintsprocessor.db.DBLibConstants
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintProcessorException
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

@Service
open class PrimaryDBLibGenericService(private val primaryNamedParameterJdbcTemplate: NamedParameterJdbcTemplate)
    : AbstractDBLibGenericService(primaryNamedParameterJdbcTemplate) {
    fun DBNameParameterJdbcTemplate(jsonNode: JsonNode): NamedParameterJdbcTemplate {
        val type = jsonNode.get("type").textValue()
        return when (type) {
            DBLibConstants.Maria_DB -> {
                mariaDBNameParameterJdbcTemplate(jsonNode)
            }
            else -> {
                throw BluePrintProcessorException("Rest adaptor($type) is not supported")
            }
        }
    }

    fun mariaDBNameParameterJdbcTemplate(jsonNode: JsonNode) : NamedParameterJdbcTemplate{

        val dataSourceBuilder = DataSourceBuilder
                .create()
                .username(jsonNode.get("username").textValue())
                .password(jsonNode.get("password").textValue())
                .url(jsonNode.get("url").textValue())
                .driverClassName("org.mariadb.jdbc.Driver")
                .build()
        return  NamedParameterJdbcTemplate(dataSourceBuilder)

    }

}