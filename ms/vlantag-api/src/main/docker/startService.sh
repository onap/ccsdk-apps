#!/bin/sh
app_args=-Dspring.config.location=${APP_CONFIG_HOME}
echo "app_args ="${app_args}
java -Djava.security.egd=file:/dev/./urandom  ${app_args} -Xms1024m -Xmx1024m  -jar  /app.jar
