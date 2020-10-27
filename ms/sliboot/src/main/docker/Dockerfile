# Prepare stage for multistage image build
## START OF STAGE0 ##
FROM @base.image.name@:@base.image.version@ AS stage0

ENV JAVA_HOME /usr/lib/jvm/java-1.8-openjdk
ENV LOG_PATH /var/log/onap/ccsdk


# copy deliverables to opt
COPY opt /opt

## END OF STAGE0 ##


FROM @base.image.name@:@base.image.version@

MAINTAINER CCSDK Team (onap-ccsdk@lists.onap.org)

ENV PROJECT_VERSION @project.version@
ENV JAVA_HOME /usr/lib/jvm/java-1.8-openjdk
ENV CCSDK_HOME /opt/onap/ccsdk
ENV CCSDK_CONFIG_DIR /opt/onap/ccsdk/config
ENV SLIBOOT_JAR @ccsdk.sliboot.jar@
ENV SVCLOGIC_PROPERTIES /opt/onap/ccsdk/config/svclogic.properties
ENV SVCLOGIC_DIR /opt/onap/ccsdk/svclogic/graphs
ENV LOG_PATH /var/log/onap/ccsdk

# Install sudo and IP utilities
RUN apk update && apk --no-cache add sudo iputils openssl


# Enable wheel group
RUN sed -e 's/# %wheel ALL=(ALL) NOPASSWD: ALL/%wheel ALL=(ALL) NOPASSWD: ALL/g' -i /etc/sudoers

# Create sli user
RUN addgroup -S sli && adduser -S sli -G sli
RUN addgroup sli wheel

COPY --from=stage0 --chown=sli:sli /opt /opt
RUN mkdir -p ${LOG_PATH} && chown sli:sli ${LOG_PATH}
RUN chmod +x ${CCSDK_HOME}/bin/*.sh

# Install ssl and java certificates
COPY $CCSDK_CONFIG_DIR/truststoreONAPall.jks $JAVA_SECURITY_DIR
RUN keytool -importkeystore -srckeystore $JAVA_SECURITY_DIR/truststoreONAPall.jks -srcstorepass changeit -destkeystore $JAVA_SECURITY_DIR/cacerts  -deststorepass changeit


USER sli
WORKDIR ${CCSDK_HOME}
ENTRYPOINT /opt/onap/ccsdk/bin/startSliboot.sh
EXPOSE 8443 8080 8181
