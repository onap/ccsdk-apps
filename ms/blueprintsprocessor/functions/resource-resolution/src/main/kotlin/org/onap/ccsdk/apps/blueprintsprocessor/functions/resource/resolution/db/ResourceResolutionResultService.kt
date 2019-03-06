package org.onap.ccsdk.apps.blueprintsprocessor.functions.resource.resolution.db

import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintConstants
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintException
import org.onap.ccsdk.apps.controllerblueprints.core.service.BluePrintRuntimeService
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import java.util.*

@Service
class ResourceResolutionResultService(private val resourceResolutionRepository: ResourceResolutionRepository) {

    fun write(properties: Map<String, Any>, result: String, bluePrintRuntimeService: BluePrintRuntimeService<*>,
              artifactPrefix: String) {

        val metadata = bluePrintRuntimeService.bluePrintContext().metadata!!

        val resourceResolutionResult = ResourceResolutionResult()
        resourceResolutionResult.id = UUID.randomUUID().toString()
        resourceResolutionResult.artifactName = artifactPrefix
        resourceResolutionResult.blueprintVersion = metadata[BluePrintConstants.METADATA_TEMPLATE_VERSION]
        resourceResolutionResult.blueprintName = metadata[BluePrintConstants.METADATA_TEMPLATE_NAME]
        resourceResolutionResult.key = properties["key"].toString()
        resourceResolutionResult.result = result

        try {
            resourceResolutionRepository.saveAndFlush(resourceResolutionResult)
        } catch (ex: DataIntegrityViolationException) {
            throw BluePrintException("Failed to store resource resolution result.", ex)
        }
    }

    fun read(properties: Map<String, Any>, bluePrintRuntimeService: BluePrintRuntimeService<*>,
             artifactPrefix: String): ResourceResolutionResult {

        val metadata = bluePrintRuntimeService.bluePrintContext().metadata!!

        val key = properties["key"].toString()
        val blueprintVersion = metadata[BluePrintConstants.METADATA_TEMPLATE_VERSION]
        val blueprintName = metadata[BluePrintConstants.METADATA_TEMPLATE_NAME]

        return resourceResolutionRepository.findByKeyAndBlueprintNameAndBlueprintVersionAndArtifactName(
            key,
            blueprintName,
            blueprintVersion,
            artifactPrefix);
    }
}