FROM --platform=linux/amd64 maven:3-eclipse-temurin-17 AS build
RUN mkdir /build
WORKDIR /build
COPY .mvn /build/.mvn/
COPY pom.xml /build/
RUN mvn dependency:copy-dependencies
COPY src /build/src/
RUN mvn package -DskipTests

FROM --platform=linux/amd64 eclipse-temurin:17
ARG APP=worker
ARG VERSION=0.0.1-SNAPSHOT
RUN mkdir /app
WORKDIR /app
COPY --from=build /build/target/${APP}-${VERSION}.jar /app/worker.jar
CMD ["java", "-jar", "/app/worker.jar"]