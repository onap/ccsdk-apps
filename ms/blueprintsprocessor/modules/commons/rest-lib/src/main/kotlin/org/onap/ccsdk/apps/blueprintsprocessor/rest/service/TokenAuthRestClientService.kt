/*
 * Copyright © 2017-2019 AT&T, Bell Canada
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

package org.onap.ccsdk.apps.blueprintsprocessor.rest.service

import org.onap.ccsdk.apps.blueprintsprocessor.rest.TokenAuthRestClientProperties
import org.onap.ccsdk.apps.blueprintsprocessor.rest.utils.WebClientUtils
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

class TokenAuthRestClientService(private val restClientProperties: TokenAuthRestClientProperties) :
    BlueprintWebClientService {

    private var webClient: WebClient? = null

    override fun webClient(): WebClient {
        if (webClient == null) {
            webClient = WebClient.builder()
                .baseUrl(restClientProperties.url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, restClientProperties.token)
                .filter(WebClientUtils.logRequest())
                .filter(WebClientUtils.logResponse())
                .build()
        }
        return webClient!!
    }
}
