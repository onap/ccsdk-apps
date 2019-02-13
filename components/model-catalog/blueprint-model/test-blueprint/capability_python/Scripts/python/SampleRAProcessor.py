from abstract_ra_processor import AbstractRAProcessor
from blueprint_constants import *
from org.onap.ccsdk.apps.blueprintsprocessor.functions.resource.resolution.utils import ResourceAssignmentUtils
from org.onap.ccsdk.apps.controllerblueprints.core import BluePrintProcessorException


class SampleRAProcessor(AbstractRAProcessor):

    def __init__(self):
        AbstractRAProcessor.__init__(self)

    def process(self, execution_request):

        AbstractRAProcessor.process(self, execution_request)
        print "Processing calling..." + PROPERTY_BLUEPRINT_BASE_PATH
        try:
            if self.ra_valid is True:
                if self.status != PROPERTY_BLUEPRINT_STATUS_FAILURE:
                    value = self.resolve_values_script(execution_request, self.value_to_resolve)
                else:
                    raise BluePrintProcessorException("Error on resource assignment. Message = " + self.error_message)
            else:
                raise BluePrintProcessorException("Error on resource assignment. Message = " + self.error_message)

            if value is not None:
                ResourceAssignmentUtils.Companion.setResourceDataValue(execution_request, self.raRuntimeService, value)
            else:
                ResourceAssignmentUtils.Companion.setFailedResourceDataValue(execution_request, "Fail to resole value")
        except BluePrintProcessorException, err:
            raise BluePrintProcessorException("Error on resource assignment. Message = " + err.message)
        return None

    def recover(self, runtime_exception, execution_request):
        AbstractRAProcessor.recover(self, runtime_exception, execution_request)
        print "Recovering calling..." + PROPERTY_BLUEPRINT_BASE_PATH
        return None

    @staticmethod
    def resolve_values_script(execution_request, value_to_resolve):
        print "Resolve value for " + value_to_resolve + " here..."
        return "test_python"
