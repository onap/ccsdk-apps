/*
 * Copyright © 2019 Bell Canada Intellectual Property.
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

import org.onap.ccsdk.apps.blueprintsprocessor.db.BluePrintDBLibGenericService
import org.onap.ccsdk.apps.blueprintsprocessor.db.DefaultDataSourceProperties
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import java.util.*
import javax.sql.DataSource

class DefaultDatabaseConfiguration(private val defaultDataSourceProperties: DefaultDataSourceProperties) : BluePrintDBLibGenericService {

    override fun namedParameterJdbcTemplate(): NamedParameterJdbcTemplate {
        return defaultNamedParameterJdbcTemplate(defaultDataSource())
    }

    override fun query(sql: String, params: Map<String, Any>): List<Map<String, Any>> {
        return defaultNamedParameterJdbcTemplate(defaultDataSource()).queryForList(sql, params)
    }

    override fun update(sql: String, params: Map<String, Any>): Int {
        return defaultNamedParameterJdbcTemplate(defaultDataSource()).update(sql, params)
    }

    val log = LoggerFactory.getLogger(PrimaryDatabaseConfiguration::class.java)!!

    fun defaultDataSource(): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName(defaultDataSourceProperties.driverClassName)
        dataSource.url = defaultDataSourceProperties.url
        dataSource.username = defaultDataSourceProperties.username
        dataSource.password = defaultDataSourceProperties.password
        return dataSource
    }

    fun defaultNamedParameterJdbcTemplate(defaultDataSource: DataSource): NamedParameterJdbcTemplate {
        return NamedParameterJdbcTemplate(defaultDataSource)
    }
}