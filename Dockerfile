#FROM openjdk:21
##WORKDIR /my-project
#COPY . .
#CMD ["./gradlew", "build"]
#
#RUN ./gradlew build
#COPY ./build/libs/*.jar /app.jar
#EXPOSE 8080
#CMD ["chmod", "777", "/app.jar"]
#ENTRYPOINT ["java", "-jar","/app.jar"]

# syntax=docker/dockerfile:experimental
FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace/app

COPY . /workspace/app
RUN --mount=type=cache,target=/root/.gradle ./gradlew clean build
RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/*-SNAPSHOT.jar)

FROM eclipse-temurin:17-jdk
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/build
#COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
#COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
#COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
COPY --from=build ${DEPENDENCY}/libs/*.jar /app.jar
#ENTRYPOINT ["java","-cp","app:app/lib/*","ProductSyncAppApplication"]
ENTRYPOINT ["java", "-jar","/app.jar"]