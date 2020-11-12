FROM maven:3.6.3-adoptopenjdk-15 as builder
WORKDIR application

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src/ ./src
RUN ls -lisah
RUN mvn package && cp target/*.jar application.jar
RUN ls -lisah
RUN java -Djarmode=layertools -jar application.jar extract

FROM adoptopenjdk:15-jre-hotspot-bionic
WORKDIR application
COPY LICENSE .
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./

EXPOSE 8080
CMD ["java", "--enable-preview", "org.springframework.boot.loader.JarLauncher"]
