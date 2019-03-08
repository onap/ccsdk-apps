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

import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintProcessorException
import org.springframework.http.HttpMethod
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient

interface BlueprintWebClientService {

    fun webClient(): WebClient

    fun <T> exchangeResource(methodType: String, path: String, request: Any, responseType: Class<T>): T {
        return exchangeResource(methodType, null, path, request, responseType)
    }

    fun <T> exchangeResource(methodType: String, headers: Map<String, String>?, path: String, request: Any,
                             responseType: Class<T>): T {
        return when (HttpMethod.resolve(methodType)) {
            HttpMethod.DELETE -> delete(path, headers, responseType)
            HttpMethod.GET -> get(path, headers, responseType)
            HttpMethod.POST -> post(path, headers, request, responseType)
            HttpMethod.PUT -> put(path, headers, request, responseType)
            else -> throw BluePrintProcessorException("Not supported methodType($methodType)")
        }
    }

    private fun <T> put(path: String, headers: Map<String, String>?, request: Any, responseType: Class<T>): T {
        return webClient().put()
            .uri(path)
            .headers { httpHeaders ->
                headers?.forEach {
                    httpHeaders.set(it.key, it.value)
                }
            }
            .body(BodyInserters.fromObject(request))
            .retrieve()
            .bodyToMono(responseType)
            .toFuture()
            .get()
    }

    private fun <T> post(path: String, headers: Map<String, String>?, request: Any, responseType: Class<T>): T {
        return webClient().post()
            .uri(path)
            .headers { httpHeaders ->
                headers?.forEach {
                    httpHeaders.set(it.key, it.value)
                }
            }
            .body(BodyInserters.fromObject(request))
            .retrieve()
            .bodyToMono(responseType)
            .toFuture()
            .get()
    }

    private fun <T> get(path: String, headers: Map<String, String>?, responseType: Class<T>): T {
        return webClient().get()
            .uri(path)
            .headers { httpHeaders ->
                headers?.forEach {
                    httpHeaders.set(it.key, it.value)
                }
            }
            .retrieve()
            .bodyToMono(responseType)
            .toFuture()
            .get()
    }

    private fun <T> delete(path: String, headers: Map<String, String>?, responseType: Class<T>): T {
        return webClient().delete()
            .uri(path)
            .headers { httpHeaders ->
                headers?.forEach {
                    httpHeaders.set(it.key, it.value)
                }
            }
            .retrieve()
            .bodyToMono(responseType)
            .toFuture()
            .get()
    }
}