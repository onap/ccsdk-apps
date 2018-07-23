FROM TBD:TBD/TBD/TBD/alpine-java:8_jdk
VOLUME /tmp
ADD NetworkElementNameGen.jar app.jar
VOLUME /opt/etc
VOLUME /opt/aai
ADD /opt/etc/ /opt/etc/
ADD /opt/aai/ /opt/aai/
ADD startService.sh /startService.sh
RUN chmod 700 /startService.sh
ENTRYPOINT ./startService.sh 