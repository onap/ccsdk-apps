from org.onap.ccsdk.apps.blueprintsprocessor.functions.resource.resolution.processor import ResourceAssignmentProcessor


class AbstractRAProcessor(ResourceAssignmentProcessor):


    def process(self, resource_assignment):

        # find all the resource with w/ property constraint as JYTHON-COMPONENT

        print "Processing calling.."
        return None

    def recover(self, runtime_exception, execution_request):
        print "Recovering calling.."
        return None
