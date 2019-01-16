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

package org.onap.ccsdk.cds.controllerblueprints.core.factory


import org.onap.ccsdk.cds.controllerblueprints.core.service.BluePrintValidatorDefaultService
import org.onap.ccsdk.cds.controllerblueprints.core.service.BluePrintValidatorService

/**
 *
 *
 * @author Brinda Santh
 */

object BluePrintValidatorFactory {

    var bluePrintValidatorServices: MutableMap<String, BluePrintValidatorService> = HashMap()

    init {
        bluePrintValidatorServices[org.onap.ccsdk.cds.controllerblueprints.core.BluePrintConstants.TYPE_DEFAULT] = BluePrintValidatorDefaultService()
    }

    fun register(key:String, bluePrintValidatorService: BluePrintValidatorService){
        bluePrintValidatorServices[key] = bluePrintValidatorService
    }

    fun instance(key : String) : BluePrintValidatorService?{
        return bluePrintValidatorServices[key]
    }

}