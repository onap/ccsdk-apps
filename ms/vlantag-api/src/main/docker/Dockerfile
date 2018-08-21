FROM anapsix/alpine-java:8_jdk
ADD vlantag-api-0.3.0-SNAPSHOT.jar app.jar
ADD startService.sh /startService.sh
RUN chmod 700 /startService.sh
ENTRYPOINT ./startService.sh 
