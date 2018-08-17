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
# This script does a sanity test on the release-name API of this micro-service.
#==================================================================================

. ./env.sh
URL=web/service/v1/genNetworkElementName
TEMP_FILE=/tmp/gen-name.$$.$RANDOM
EXTERNAL_KEY=${2:-123456789}

USE_DB=${1:-'Y'}
USE_DB_BOOL=$([ "$USE_DB" == "Y" ] && echo "true" || echo "false")

printf '{ "UseDb": "' > $TEMP_FILE
printf $USE_DB_BOOL >> $TEMP_FILE
printf '", "elements": [ { "external-key": "sanity-' >> $TEMP_FILE
printf $EXTERNAL_KEY >> $TEMP_FILE
printf '", "resource-name": "sanity-1" } ] }' >> $TEMP_FILE

echo "==================================================="
echo "======= Releasing name with request: =============="
cat $TEMP_FILE
echo ""
echo "==================================================="

echo "==================================================="
curl -vi -X "DELETE" -H "Content-Type: application/json" --data @$TEMP_FILE $PROTOCOL://$HOST:$PORT/$URL
echo "==================================================="

rm -f $TEMP_FILE

