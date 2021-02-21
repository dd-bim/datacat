# datacat API

This project offers a GraphQL-API to manage and query a data
catalog closely structured after the ISO 12006-3:2007 specification.
The application is based on [Spring Boot](https://spring.io/projects/spring-boot) 
and uses [Maven](http://maven.apache.org) as a package and dependency 
management framework. The API layer is based on the [graphql-java](https://www.graphql-java.com) 
library.

For development, a local Docker installation is needed. Also, docker-compose should be
used to orchestrate all runtime dependencies. 

Please check the datacat editor project for an example of a client
application that interacts with this API.

# Dependencies

The catalog is persisted via a Neo4j database backend. Additionally, an SMTP server is
needed to send email notifications to users during on-boarding. These components need to 
be available at boot time for the Spring Boot application to work. 
The Docker image of the API component comes with the helper script `wait-for-it` preinstalled 
to check the availability of these components at boot time gracefully.

Check the provided `docker-compose.yml` for an example of a start command that waits
for the database and SMTP components to be ready.

**Please be aware, that the authorization settings provided with this example are
default settings. Never use these credentials in production.**

# Development

This project offers a very simple Docker Compose configuration that can be
used to start up all required components to work on the API locally. 

Run the following command to build and run the application as a Docker image and 
execute it locally with a newly initialized Neo4j database.

You can access the [MailSlurper](https://mailslurper.com) UI via http://localhost:9080 
to check for emails send by the API. Be aware, that this tool is for local testing only,
emails are not persisted or relayed.

````bash
$ docker-compose up -d
````

If you're working on the API locally, you may only start the required database and SMTP
backend for testing:

````bash
$ docker-compose db mail -d
`````

Check health and availability via `docker-compose ps`.

Most IDEs make it easy to start a Spring Boot application. Check the documentation of
your working environment. Alternatively, you can start the application using the included
maven wrapper scripts:

````bash
$ ./mvnw spring-boot:run
````

If you use the docker-compose configuration described above, you'll need to provide
the appropriate connection parameters. You can add additional configuration settings
as described in the Spring Boot documentation.

````bash
$ ./mvnw spring-boot:run -Dspring-boot.run.arguments="\
    --logging.level.de.bentrm=DEBUG \
    --spring.mail.host=localhost \
    --spring.mail.port=2500 \
    --spring.data.neo4j.username=neo4j \
    --spring.data.neo4j.password=s3cret"
````

Follow the 
[Spring Boot documentation](https://docs.spring.io/spring-boot/docs/2.4.2/reference/html/spring-boot-features.html#boot-features-external-config)
on how to set the parameters either by environmental variable, 
startup parameter or by overriding the default configuration.

# Create new Docker image

The preferred way to install the application stack for production is to run the components
as isolated docker images. To create an image from the current source code, run the 
following command:

````bash
$ docker build . -t bentrm/datacat:${version} -t bentrm/datacat:latest
````

A Github action is configured to build new images with every push to the master branch as well as for
every release that follows the v*.*.* naming convention.

The image should be hosted with a centralized package registry. Current images are available
at [Docker Hub](https://hub.docker.com/repository/docker/bentrm/datacat).
The version tag should equal the current git tag.

An example configuration for production use is available on [Github](https://github.com/dd-bim/datacat-stack).

# Assign user roles

Currently, users can register themselves via clients. After validating their email address
they will be assigned the role of a read-only users. Only admin users are allowed to verify
users for write access. The easiest way to do this is by using the /graphiql interface of the
datacat editor client:

````graphql
mutation {
  updateAccountStatus(input: {
    username: "test"
    status: Verified
  }) {
    username
    status
  }
}
````

This mutation will assign the "USER" role to the user.


# Backup & Restore

To back up user and application data you can mount the database volume in an auxiliary 
container and tar the data directory. The following command 

* starts a basic, temporary ubuntu docker container,
* maps the same data volumes as used by the database container configured in the example `docker-compose.yml`
* and tars the contents of the data directory in a local directory called `backups`.

````bash
$ docker run --rm --volumes-from db -v ~/backups:/backups ubuntu \
  bash -c "cd /data && tar cfvz /backups/$(date +'%Y-%m-%d-%H-%M-%S')-datact-backup.tar.gz ."
````

Consider moving the backup archive to a safe location other than the current host.

To restore this backup

* start the application stack to initialize a new data volume
* stop the application stack, so you can safley write to the data volume from an auxiliary container
* restore the backup from the tar file (**all content will be overwritten**)
* restart the application stack

````bash
$ docker run --rm --volumes-from db -v ~/backups:/backups ubuntu \
  bash -c "cd /data && tar xfvz /backups/2021-02-21-14-19-35-datact-backup.tar.gz ."
````
