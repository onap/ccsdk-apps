from abstract_blueprint_function import AbstractPythonComponentFunction
from blueprint_constants import *

class SamplePythonComponentNode(AbstractPythonComponentFunction):

    def process(self, execution_request):
        print "Processing calling.." + self.bluePrintRuntimeService.getNodeTemplateOperationOutputValue("resource-assignment", "ResourceResolutionComponent","process", "resource-assignment-params" ).get("baseconfig").asText()
        return None

    def recover(self, runtime_exception, execution_request):
        print "Recovering calling.." + PROPERTY_BLUEPRINT_BASE_PATH
        return None
