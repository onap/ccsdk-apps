from abstract_ra_processor import AbstractRAProcessor


class SampleRAProcessorFunction(AbstractRAProcessor):

    def process(self, execution_request):
        print "Processing calling.."

        super.process()
        map.forEach( doThat())
        return None

    def recover(self, runtime_exception, execution_request):
        print "Recovering calling.."
        return None
