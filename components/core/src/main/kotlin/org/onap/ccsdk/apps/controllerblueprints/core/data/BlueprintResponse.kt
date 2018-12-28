package org.onap.ccsdk.apps.controllerblueprints.core.data

import com.fasterxml.jackson.annotation.JsonRootName


/**
 *This class provide all the Model responses for Blueprint Archive operation in REST API
 *
 * Add more class here if needed
 *
 * @author Steve Siani
 */

@JsonRootName("item_blueprint_response")
open class BlueprintInfoResponse(var id:  String,
                                 var name: String?,
                                 var description: String?,
                                 var state: Int?,
                                 var version: String?)

open class BlueprintFileResponse(id: String, name: String?, description: String?,
                                 state: Int?, version: String, var file: ByteArray):
                                BlueprintInfoResponse(id, name, description, state, version)