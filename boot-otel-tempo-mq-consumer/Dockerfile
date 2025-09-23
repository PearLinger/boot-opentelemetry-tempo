FROM mnadeem/boot-otel-tempo-docker:0.0.1-SNAPSHOT
ENV APP_NAME boot-otel-tempo-mq-consumer

COPY ./target/${APP_NAME}-*.jar ${APP_HOME}/${APP_NAME}.jar

# EXPOSE 5000 5001
# ENV JAVA_TOOL_OPTIONS "-Dcom.sun.management.jmxremote.ssl=false \
# -Dcom.sun.management.jmxremote.authenticate=false \
# -Dcom.sun.management.jmxremote.port=5000 \
# -Dcom.sun.management.jmxremote.rmi.port=5001 \
# -Dcom.sun.management.jmxremote.host=127.0.0.1  \
# -Djava.rmi.server.hostname=127.0.0.1"
COPY ./scripts/wait-for.sh ${APP_HOME}/
RUN ["chmod", "+x", "/app/bin/wait-for.sh"]
EXPOSE 8091
# OpenTelemetry:
# https://github.com/open-telemetry/opentelemetry-java-instrumentation
ENV JAVA_OPTS "${JAVA_OPTS} \
  -Dotel.traces.exporter=otlp \
  -Dotel.exporter.otlp.endpoint=http://tempo:4317 \
  -Dotel.exporter.otlp.protocol=grpc \
  -Dotel.metrics.exporter=none \
  -Dotel.resource.attributes="service.name=${APP_NAME}" \
  -Dotel.javaagent.debug=false \
  -javaagent:${APP_HOME}/${OTEL_AGENT_JAR_FILE}"