# datacat API

The datacat API offers a GraphQL-interface to manage and query a data
catalog closely structured after the ISO 12006-3:2007 specification.

Please check the datacat editor project for an example of a client
applicaion that interacts with the API.

# Dependencies

The API uses a Neo4j database and a SMTP server to send notifications
to user. These components need to be available at boot time.  

# Development

This project offers a very simple Docker Compose configuration that can be
used to start up required components to work on the API locally.

````bash
$ docker-compose up -d
````

After the containers have been initialized, you may start the Spring application
with the following default settings:

````
logging.level: DEBUG
spring.mail.host: localhost
spring.mail.port: 2500
spring.data.neo4j.username: neo4j
spring.data.neo4j.password: s3cret
````

Follow the [Spring Boot documentation](https://docs.spring.io/spring-boot/docs/2.4.2/reference/html/spring-boot-features.html#boot-features-external-config) on how to set the parameters
either by environmental variable, startup parameter or by overriding the default configuration.


