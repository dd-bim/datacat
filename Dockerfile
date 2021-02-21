FROM maven:3.6.3-adoptopenjdk-15 as builder
WORKDIR application

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src/ ./src
RUN mvn package && cp target/*.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM adoptopenjdk:15-jre-hotspot-bionic
WORKDIR application
COPY LICENSE .
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./

EXPOSE 8080

HEALTHCHECK --start-period=30s --interval=30s --timeout=3s --retries=3 \
    CMD curl --fail --silent localhost:8080/actuator/health | grep -L DOWN && exit 1
CMD ["java", "--enable-preview", "org.springframework.boot.loader.JarLauncher"]
