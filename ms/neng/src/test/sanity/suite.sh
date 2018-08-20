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
# This script does a sanity test on the APIs of the micro-service.
#==================================================================================

EXTERNAL_KEY=$(date +%s)$RANDOM

./hello.sh
./add-policy.sh $EXTERNAL_KEY
./get-policy.sh $EXTERNAL_KEY
./gen-name.sh Y $EXTERNAL_KEY
./release-name.sh Y $EXTERNAL_KEY

