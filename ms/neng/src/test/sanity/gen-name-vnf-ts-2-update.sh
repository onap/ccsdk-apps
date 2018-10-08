#!/bin/bash
#============LICENSE_START=======================================================
#  ONAP : CCSDK.apps
#  ================================================================================
#  Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
#  ================================================================================
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#  
#       http://www.apache.org/licenses/LICENSE-2.0
#  
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#============LICENSE_END=========================================================

#==================================================================================
# This script does a sanity test on the generate-name API of this micro-service.
#==================================================================================

. ./env.sh
URL=web/service/v1/genNetworkElementName
TEMP_FILE=/tmp/gen-name.$$.$RANDOM
EXTERNAL_KEY=$RANDOM

USE_DB=${1:-'Y'}
USE_DB_BOOL=$([ "$USE_DB" == "Y" ] && echo "true" || echo "false")

printf '{ "UseDb": "' > $TEMP_FILE
printf $USE_DB_BOOL >> $TEMP_FILE
printf '", "elements": [ { "external-key": "sanity-' >> $TEMP_FILE
printf $EXTERNAL_KEY >> $TEMP_FILE
printf '", "policy-instance-name": "vnf-policy-ts-2", "NF_NAMING_CODE": "me9", "COMPLEX": "dlstxa", ' >> $TEMP_FILE 
printf '"resource-name": "VNF2", "resource-value": "ASDF' >> $TEMP_FILE
printf $EXTERNAL_KEY >> $TEMP_FILE
printf '", "naming-type": "VNF2" } ] }' >> $TEMP_FILE

echo "==================================================="
echo "======== Generating name with request: ============"
echo ""
echo ""
cat $TEMP_FILE
echo ""
echo ""
echo ""
echo "==================================================="

echo "==================================================="
curl -vi -H "Content-Type: application/json" --data @$TEMP_FILE $PROTOCOL://$HOST:$PORT/$URL
echo ""
echo ""
echo "==================================================="

rm -f $TEMP_FILE


