/*-
 * ============LICENSE_START=======================================================
 * ONAP - CDS
 * ================================================================================
 * Copyright (C) 2019 Huawei Technologies Co., Ltd. All rights reserved.
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

package org.onap.ccsdk.apps.blueprintsprocessor.dmaap

import com.att.nsa.mr.client.MRBatchingPublisher
import com.att.nsa.mr.client.MRClientFactory
import com.att.nsa.mr.client.MRPublisher
import java.io.File

import java.util.Properties
import java.util.concurrent.TimeUnit

/**
 * Representation of DMAAP event publisher, to create a session with the
 * message router and send messages when asked for. The producer.properties
 * is used for creating a session. In order to overwrite the parameters such
 * as host, topic, username and password, the event.properties can be used.
 *
 * compName : Name of the component appended in the event.properties file
 * to overwrite.
 * (E.g., SO.event.topic=CDS_SO : In this "SO" is the component name)
 * TODO : Exception handling and UT
 */
class DmaapEventPublisher(compName: String = ""): EventPublisher {

    /**
     * Path of the event.properties file.
     */
    val eventPath: String = "/event.properties"

    /**
     * Path of the producer.properties file.
     */
    val prodPath: String = "/producer.properties"

    /**
     * List of topics for a given message to be sent.
     */
    var topics = mutableListOf<String>()

    /**
     * List of clients formed for the list of topics where the messages has to
     * be sent.
     */
    private var clients = mutableListOf<MRBatchingPublisher>()

    /**
     * Final properties to create the session.
     */
    private var props: Properties = Properties()


    /**
     * Loads the event.properties file and populates the finalized parameters
     * such as host, topic, username and password if available for that
     * specified component. Later loads the producer.properties and creates
     * list of clients for each topic.
     */
    init {
        val eventProps = loadPropertiesFile(eventPath)
        parseEventProps(compName, eventProps)
        getClients()
    }

    private fun getClients() {
        val defProps = loadPropertiesFile(prodPath)
        defProps.putAll(props)

        for (topic in topics) {
            defProps.setProperty("topic", topic)
            val client = MRClientFactory.createBatchingPublisher(defProps)
            clients.add(client)
        }
    }

    private fun parseEventProps(cName: String, eProps: Properties) {
        val topic = eProps.get("$cName.event.topic").toString()
        if (topic != "") {
            topics.addAll(topic.split(","))
        }

        val host = eProps.get("$cName.event.pool.members").toString()
        val userName = eProps.get("$cName.event.username").toString()
        val password = eProps.get("$cName.event.password").toString()

        props.setProperty("host", host)
        props.setProperty("username", userName)
        props.setProperty("password", password)

        if (userName == "" && password == "") {
            props.setProperty("TransportType", "HTTPNOAUTH")
        }
    }

    private fun loadPropertiesFile(path: String): Properties {
        val props = Properties()
        val file = File(path)
        if (file.exists()) {
            props.load(file.inputStream())
        }
        return props
    }

    override fun sendMessage(partition: String , messages: Collection<String>):
            Boolean {
        val success = true
        val dmaapMessages = mutableListOf<MRPublisher.message>()
        for (m in messages) {
            dmaapMessages.add(MRPublisher.message(partition, m))
        }
        for (client in clients) {
            client.send(dmaapMessages)
        }
        return success
    }

    override fun close() {
        if (!clients.isEmpty()) {
            for (client in clients) {
                client.close(1, TimeUnit.SECONDS)
            }
        }
    }

}