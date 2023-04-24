FROM --platform=linux/amd64 maven:3-eclipse-temurin-17 AS build
ARG MVN_MIRROR
RUN mkdir /build
WORKDIR /build
COPY generate-settings.sh /build
RUN MVN_MIRROR=$MVN_MIRROR bash /build/generate-settings.sh
COPY .mvn /build/.mvn/
COPY pom.xml /build/
RUN mvn dependency:copy-dependencies
COPY src /build/src/
RUN mvn package -DskipTests

FROM --platform=linux/amd64 eclipse-temurin:17
ARG APP=workercontroller
ARG VERSION=0.0.1-SNAPSHOT
RUN mkdir /app
WORKDIR /app
COPY --from=build /build/target/${APP}-${VERSION}.jar /app/workercontroller.jar
CMD ["java", "-jar", "/app/workercontroller.jar"]
