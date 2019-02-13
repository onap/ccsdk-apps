from abstract_ra_processor import AbstractRAProcessor
from blueprint_constants import *
from java.lang import Exception
from com.fasterxml.jackson.databind.node import NullNode


def python_external_script(execution_request):
    print "Prepare valueNode here..."
    value_node = NullNode
    return value_node


class SampleRAProcessor(AbstractRAProcessor):

    def __init__(self):
        AbstractRAProcessor.__init__(self)

    def process(self, execution_request):
        AbstractRAProcessor.process(self, execution_request)
        if self.ra_valid is True:
            if self.error_message is not None:
                python_external_script(execution_request)
            else:
                raise Exception("Error on resource assignment. Message = " + self.error_message)
        else:
            raise Exception("Error on resource assignment. Message = " + self.error_message)

        print "Processing calling..." + PROPERTY_BLUEPRINT_BASE_PATH
        return None

    def recover(self, runtime_exception, execution_request):
        AbstractRAProcessor.recover(self, runtime_exception, execution_request)
        print "Recovering calling..." + PROPERTY_BLUEPRINT_BASE_PATH
        return None
