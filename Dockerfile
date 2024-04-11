#FROM eclipse-temurin:17-jdk
#WORKDIR /app
#CMD ["./gradlew", "clean","bootjar"]
#COPY build/libs/*.jar /app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]

# syntax=docker/dockerfile:experimental

FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace/app

COPY . /workspace/app
RUN --mount=type=cache,target=/root/.gradle ./gradlew clean build
RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/*-SNAPSHOT.jar)

FROM eclipse-temurin:17-jdk

VOLUME /tmp
ARG DEPENDENCY=/workspace/app/build
COPY --from=build ${DEPENDENCY}/libs/*.jar /app.jar
#COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
#COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

#ENTRYPOINT ["java","-cp","app:app/lib/*","ProductSyncAppApplication"]
ENTRYPOINT ["java","-jar","/app.jar"]