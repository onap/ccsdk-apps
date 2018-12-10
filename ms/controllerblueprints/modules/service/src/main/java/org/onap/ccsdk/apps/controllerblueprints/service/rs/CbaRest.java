/*
 * Copyright © 2018 IBM Intellectual Property.
 * Modifications Copyright © 2018 IBM.
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

package org.onap.ccsdk.apps.controllerblueprints.service.rs;

import org.onap.ccsdk.apps.controllerblueprints.service.CbaService;
import org.onap.ccsdk.apps.controllerblueprints.service.model.UploadCbaResponse;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * CbaRest.java Purpose: Provide a REST API to upload single and multiple CBA
 *
 * @author Steve Siani
 * @version 2.0
 * {@inheritDoc}
 */
@RestController
@RequestMapping(value = "/api/v1/cba")
public class CbaRest {

    private CbaService cbaService;

    /**
     * This is the CbaRest constructor.
     *
     * @param cbaService Cba Service
     */
    public CbaRest(CbaService cbaService) {
        this.cbaService = cbaService;
    }

    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public  Flux<UploadCbaResponse> saveCBA(@RequestBody Flux<Part> parts) {
        return parts.filter(part -> part instanceof FilePart) // only retain file parts
            .ofType(FilePart.class) // convert the flux to FilePart
            .flatMap(cbaService::saveCBAFile); // save each file and flatmap it to a flux of results
    }

    @DeleteMapping(path = "/{name}")
    public void deleteCBA(@PathVariable(value = "name") String name) {
        this.cbaService.deleteCBA(name);
    }
}
