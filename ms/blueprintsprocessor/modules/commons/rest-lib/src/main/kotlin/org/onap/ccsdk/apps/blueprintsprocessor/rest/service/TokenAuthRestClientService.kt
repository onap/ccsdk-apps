/*
 * Copyright © 2019 Bell Canada
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
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

class TokenAuthRestClientService(private val restClientProperties: TokenAuthRestClientProperties) :
    BlueprintWebClientService {

    override fun defaultHeaders(): Map<String, String> {
        return mapOf(
                HttpHeaders.CONTENT_TYPE to MediaType.APPLICATION_JSON_VALUE,
                HttpHeaders.ACCEPT to MediaType.APPLICATION_JSON_VALUE,
                HttpHeaders.AUTHORIZATION to restClientProperties.token!!)
    }

    override fun exchangeResource(methodType: String, path: String, request: String, headers: Map<String, String>): String {
        val headersWithAuthorization = addAuthorizationHeader(headers)
        return super.exchangeResource(methodType, path, request, headersWithAuthorization)
    }

    private fun addAuthorizationHeader(headers: Map<String, String>): Map<String, String> {
        val customHeaders: MutableMap<String, String> = headers.toMutableMap()
        customHeaders[HttpHeaders.AUTHORIZATION] = restClientProperties.token!!
        return customHeaders
    }

    override fun host(uri: String): String {
        return restClientProperties.url + uri
    }
}
