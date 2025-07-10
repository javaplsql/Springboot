FROM registry.global.ccc.srvb.can.paas.cloudcenter.corp/produban/javase-17-ubi8:latest as builder
COPY --chown=java:java target/*.jar /tmp/
# find application jar & extract layers
RUN export JAR_PATH=`find /tmp -maxdepth 1 -name "*jar" | sort | tail -1` && \
    echo "JAR_PATH: $JAR_PATH" && \
    java -Djarmode=layertools -jar $JAR_PATH extract --destination /tmp/app

FROM registry.global.ccc.srvb.can.paas.cloudcenter.corp/produban/javase-17-ubi8:latest
ARG EXTRACTED=/tmp/app
WORKDIR /opt/app
# Copy layers
COPY --from=builder ${EXTRACTED}/dependencies/ ./
COPY --from=builder ${EXTRACTED}/spring-boot-loader/ ./
COPY --from=builder ${EXTRACTED}/snapshot-dependencies/ ./
COPY --from=builder ${EXTRACTED}/application/ ./
# Using a non-root user
USER 20000:20000
# Execute java
# We need to rewrite entrypoint because image base only support jar executions
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS_EXT} org.springframework.boot.loader.launch.JarLauncher $JAVA_PARAMETERS ${@}"]

