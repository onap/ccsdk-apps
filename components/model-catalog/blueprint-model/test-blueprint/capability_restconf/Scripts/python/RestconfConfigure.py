# ============LICENSE_START=======================================================
#  Copyright (C) 2019 Nordix Foundation.
# ================================================================================
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# SPDX-License-Identifier: Apache-2.0
# ============LICENSE_END=========================================================

from org.onap.ccsdk.apps.blueprintsprocessor.functions.restconf.executor import \
    RestconfComponentFunction
from java.lang import Exception as JavaException


class RestconfConfigure(RestconfComponentFunction):

    def process(self, execution_request):
        log = globals()["log"]
        log.info("Started execution of process method")
        try:
            log.info("getting pnf id")
            pnf_id = execution_request.payload.get("configure-request").get("configure-properties").get("pnf-id")
            pnf_id = str(pnf_id).strip('\"')
            log.info("pnf-id: {}", pnf_id)

            log.info("defining mount payload")
            mount_payload = self.resolveAndGenerateMessage("configure-mapping", "configure-template")
            log.info("mount payload: \n {}", mount_payload)

            # defining custom header
            headers = {
                "Content-Type": "application/xml"
            }

            log.info("will test sending put request")
            url = "restconf/config/network-topology:network-topology/topology/topology-netconf/node/" + str(pnf_id)
            log.info("trying to call url: {}", url)
            web_client_service = self.restClientService("sdncodl")
            result = web_client_service.exchangeResource("PUT", url, mount_payload, headers)
            log.info("result: {}", result)
            log.info("Ended execution of process method")

        except JavaException, err:
            log.error("Java Exception in the script", err)
            raise err
        except Exception, err:
            log.error("Python Exception in the script", err)
            raise err


def recover(self, runtime_exception, execution_request):
        log = globals()["log"]
        log.info("Recover method, no code to execute")
        return None
