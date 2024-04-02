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
#FROM eclipse-temurin:21-jdk-alpine AS build
#WORKDIR /workspace/app
#
#COPY . /workspace/app
#RUN --mount=type=cache,target=/root/.gradle ./gradlew clean build
#RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/*-SNAPSHOT.jar)
#
#FROM eclipse-temurin:21-jdk-alpine
#VOLUME /tmp
#ARG DEPENDENCY=/workspace/app/build/dependency
#COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
#COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
#COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
#ENTRYPOINT ["java","-cp","app:app/lib/*","hello.Application"]