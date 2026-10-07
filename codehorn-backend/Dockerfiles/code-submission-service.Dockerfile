# syntax=docker/dockerfile:1
FROM eclipse-temurin:25 AS jre-build

COPY ./gradlew ./settings.gradle.kts ./build.gradle.kts /app/codehorn/
COPY ./gradle /app/codehorn/gradle
# Copy all subproject build files while preserving directory hierarchy
COPY --parents **/build.gradle.kts /app/codehorn/

#COPY ./common/build.gradle.kts /app/codehorn/common/build.gradle.kts
#COPY ./gateway-service/build.gradle.kts /app/codehorn/gateway-service/build.gradle.kts
#COPY ./problem-submission-service/build.gradle.kts /app/codehorn/problem-submission-service/build.gradle.kts
#COPY ./problems-service/build.gradle.kts /app/codehorn/problems-service/build.gradle.kts
#COPY ./contest-service/build.gradle.kts /app/codehorn/contest-service/build.gradle.kts
#COPY ./daily-challenge-service/build.gradle.kts /app/codehorn/daily-challenge-service/build.gradle.kts
#COPY ./user-service/build.gradle.kts /app/codehorn/user-service/build.gradle.kts
#COPY ./auth-service/build.gradle.kts /app/codehorn/auth-service/build.gradle.kts
#COPY ./code-execution-service/build.gradle.kts /app/codehorn/code-execution-service/build.gradle.kts
#COPY ./code-submission-service/build.gradle.kts /app/codehorn/code-submission-service/build.gradle.kts
#COPY ./c-execution-service/build.gradle.kts /app/codehorn/c-execution-service/build.gradle.kts
#COPY ./cpp-execution-service/build.gradle.kts /app/codehorn/cpp-execution-service/build.gradle.kts
#COPY ./java-execution-service/build.gradle.kts /app/codehorn/java-execution-service/build.gradle.kts
#COPY ./python-execution-service/build.gradle.kts /app/codehorn/python-execution-service/build.gradle.kts
#COPY ./javascript-execution-service/build.gradle.kts /app/codehorn/javascript-execution-service/build.gradle.kts
#COPY ./common-code-execution-service/build.gradle.kts /app/codehorn/common-code-execution-service/build.gradle.kts

WORKDIR /app/codehorn

RUN --mode=type=bind,from=host-gradle-cache,target=/root/.gradle/caches,rw \
    --mode=type=bind,from=host-gradle-wrapper,target=/root/.gradle/wrapper,rw \
    chmod +x ./gradlew && ./gradlew dependencies

ARG CODEHORN_APP_VERSION

RUN chmod +x ./gradlew \
    && ./gradlew :code-submission-service:build -x test \
    && mv /app/codehorn/code-submission-service/build/libs/code-submission-service-${CODEHORN_APP_VERSION}.jar /app/codehorn/code-submission-service.jar \
    && apt update \
    && apt install unzip -y \
    && unzip /app/codehorn/code-submission-service.jar -d temp

RUN $JAVA_HOME/bin/jdeps \
      --print-module-deps \
      --ignore-missing-deps \
      --recursive \
      --multi-release 25 \
      --class-path="./temp/BOOT-INF/lib/*" \
      --module-path="./temp/BOOT-INF/lib/*" \
      /app/codehorn/code-submission-service.jar > ./jre-modules.txt \
    && $JAVA_HOME/bin/jlink \
      --verbose \
      --add-modules "$(cat ./jre-modules.txt)" \
      --strip-debug \
      --no-man-pages \
      --no-header-files \
      --compress=2 \
      --output /tmp/jre \
    && rm -rf temp

FROM debian:bookworm-slim
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH "${JAVA_HOME}/bin:${PATH}"
COPY --from=jre-build /tmp/jre $JAVA_HOME
COPY --from=jre-build /app/codehorn/code-submission-service.jar /app/code-submission-service.jar

RUN apt-get update && apt-get install curl -y

EXPOSE 80

CMD ["java", "-Dserver.port=80", "-jar", "/app/code-submission-service.jar"]
