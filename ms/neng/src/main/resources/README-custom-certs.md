Any .pem files in this directory will be copied to /etc/ssl/certs on the
docker container and installed prior to running apk.  This might be needed,
for example, for docker builds to work properly behind a corporate firewall.
