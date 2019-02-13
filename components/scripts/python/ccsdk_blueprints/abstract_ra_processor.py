from org.onap.ccsdk.apps.blueprintsprocessor.functions.resource.resolution.processor import ResourceAssignmentProcessor
from blueprint_constants import *
from com.fasterxml.jackson.databind.node import NullNode
from resource_assignment_utils import ResourceAssignmentUtils
from java.lang import Exception


class AbstractRAProcessor(ResourceAssignmentProcessor):

    def __init__(self):
        ResourceAssignmentProcessor.__init__(self)
        self.status = PROPERTY_BLUEPRINT_STATUS_SUCCESS
        self.error_message = None
        self.ra_valid = False

    def process(self, execution_request):
        print "Processing calling from parent..."
        try:
            self.ra_valid = self.validate(execution_request)
            ra_runtime = self.raRuntimeService
            value = ra_runtime.getInputValue(execution_request.name)
            if value is not None and value is not NullNode:
                ResourceAssignmentUtils.set_ressource_data_value(execution_request, ra_runtime, value)
            else:
                pass
        except Exception, err:
            self.status = PROPERTY_BLUEPRINT_STATUS_FAILURE
            self.error_message = "Get Running python scripting Failure :" + err.getMessage()

    def recover(self, runtime_exception, execution_request):
        print "Recovering calling from parent.."
        return None

    @staticmethod
    def validate(ra):
        if ra.name is None or ra.name is None:
            raise Exception("Failed getting value for template key (" + ra.name + ") and " +
                            "dictionary key (" + ra.dictionaryName +
                            ") of type (" + ra.type + ")")
        else:
            pass
        return True

    def get_status(self): return self.status

    def set_status(self, value): self.status = value

    def get_error_message(self): return self.error_message

    def set_error_message(self, value): self.error_message = value

    def get_ra_valid(self): return self.ra_valid

