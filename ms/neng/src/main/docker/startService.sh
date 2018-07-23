#!/bin/sh
touch /app.jar
app_args=-Dspring.profiles.active=${SPRING_PROFILE}
app_args=${app_args}" -Dpolicymgr_url="${POLICYMGR_URL}" -Dinstar_aaf_enc_pass="${AAF_PASS_ENC}
app_args=${app_args}" -Daai_cert_pass="${AAI_CERT_PASSWORD}" -Daai_uribase="${AAI_URI_BASE}
app_args=${app_args}" -Dnwelgen_db_user="${NWEG_DB_USER}" -Dnwelgen_db_pass="${NWEG_DB_PASS}" -Dnwelgen_db_url="${NWEG_DB_URL}
echo "app_args ="${app_args}
echo "app_args   ="${app_args}
java -Djava.security.egd=file:/dev/./urandom  ${app_args} -Xms1024m -Xmx1024m  -jar  /app.jar
