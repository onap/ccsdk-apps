#!/bin/sh
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

touch /app.jar
APP_ARGS=-Dspring.profiles.active=${SPRING_PROFILE}
APP_ARGS=${APP_ARGS}" -Dneng_db_user="${NENG_DB_USER}" -Dneng_db_pass="${NENG_DB_PASS}         
APP_ARGS=${APP_ARGS}" -Dneng_db_url="${NENG_DB_URL}                                        
APP_ARGS=${APP_ARGS}" -Dpol_client_auth="${POL_CLIENT_AUTH}                           
APP_ARGS=${APP_ARGS}" -Dpol_basic_auth="${POL_BASIC_AUTH}  
APP_ARGS=${APP_ARGS}" -Dpol_url="${POL_URL}                
APP_ARGS=${APP_ARGS}" -Dpol_env="${POL_ENV}              
APP_ARGS=${APP_ARGS}" -Dpol_req_id="${POL_REQ_ID}
APP_ARGS=${APP_ARGS}" -Daai_cert_pass="${AAI_CERT_PASS}
APP_ARGS=${APP_ARGS}" -Daai_cert_path="${AAI_CERT_PATH}
APP_ARGS=${APP_ARGS}" -Daai_uri="${AAI_URI}            
APP_ARGS=${APP_ARGS}" -cp /opt/etc/config"

echo "APP_ARGS ="${APP_ARGS}
java -Djava.security.egd=file:/dev/./urandom  ${APP_ARGS} -Xms1024m -Xmx1024m -jar /app.jar --spring.config.location=/opt/etc/config/ > /tmp/app.out 2> /tmp/app.err

