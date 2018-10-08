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
# This script does a sanity test on the add-policy API of the micro-service.
#==================================================================================

. ./env.sh
URL=web/service/v1/addPolicy
TEMP_FILE=/tmp/add-policy.$$.$RANDOM
EXTERNAL_KEY=${1:-123456789}

printf '{ ' > $TEMP_FILE
printf '"policyName": "vnf-policy-2' >> $TEMP_FILE
printf '", "policyValue" : "' >> $TEMP_FILE
cat ./policy-vnf-2.json | sed 's/\"/\\\"/g' | tr '\n' ' ' | tr '\r' ' ' >> $TEMP_FILE
echo '"}' >> $TEMP_FILE

echo "==================================================="
echo "======  Adding Policy:  ==========================="
cat $TEMP_FILE
echo ""
echo "==================================================="

echo "==================================================="
curl -vi -H "Content-Type: application/json" --data @$TEMP_FILE $PROTOCOL://$HOST:$PORT/$URL
echo "==================================================="

rm -f $TEMP_FILE

