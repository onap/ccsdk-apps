#!/bin/sh

nodeName=BlueprintsProcessor_1.0.0_$(cat /proc/self/cgroup | grep docker | sed s/\\//\\n/g | tail -1)

echo "APP Config HOME : ${APP_CONFIG_HOME}"
export APP_HOME=/opt/app/onap

cp -rf /config /opt/app/onap/
cp -rf /app /opt/app/onap/config
source /etc/run.source
