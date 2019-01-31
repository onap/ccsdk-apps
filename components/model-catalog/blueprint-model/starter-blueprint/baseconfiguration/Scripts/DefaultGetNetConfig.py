import  netconf_constant
from netconfclient import NetconfClient
from java.lang import Exception
from abstract_blueprint_function import AbstractPythonComponentFunction


class DefaultGetNetConfig(AbstractPythonComponentFunction):
    def process(self, execution_request):
        try:
            log = globals()[netconf_constant.SERVICE_LOG]
            print(globals())
            #requestId = globals()[netconf_constant.PARAM_REQUEST_ID]
            requestId = '1234'
            #messageService = globals()[netconf_constant.SERVICE_MESSAGE]
            netconfService = globals()['netconf-rpc-service']
            #deviceInfo = globals()['deviceInfo']


            log.info("Executing Python Default Get Config")

            nc = NetconfClient(log, netconfService)
            #mc = MessageClient(log, messageService)

            runningconfigTemplateName = "runningconfig-template"
            runningConfigTemplate = 'C:\Users\vn166g\Documents\1902\ONAPProject\apps\ms\blueprintsprocessor\functions\netconf-executor\src\test\resources\requests\sample-activate-request.json'


            runningConfigMessageId = "get-config-" + requestId
            #nc.connect(deviceInfo)
            deviceResponse = nc.getConfig(messageId=runningConfigMessageId,
                                          filter=runningConfigTemplate)

            log.info("Get Running Config Response {} ", deviceResponse.responseMessage)
            if(deviceResponse !='null') :
                status = deviceResponse.status
                responseData = "{}"
                if (deviceResponse.status != netconf_constant.STATUS_SUCCESS and deviceResponse.errorMessage != 'null'):
                    errorMessage = "Get Running Config Failure ::"+ deviceResponse.errorMessage

        except Exception, err:
            log.info("Exception in the script {}",err.getMessage())
            status = netconf_constant.STATUS_FAILURE
            errorMessage = "Get Running Config Failure ::"+err.getMessage()

    def  recover(self, runtime_exception, execution_request):
        print "Recovering calling.." + PROPERTY_BLUEPRINT_BASE_PATH
        return None

