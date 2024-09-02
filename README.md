# Demo Spring Project

[![CI](https://github.com/koranke/backend-playground/actions/workflows/test.yml/badge.svg)](https://github.com/koranke/backend-playground/actions/workflows/test.yml)
[![CI-With-System-Tests](https://github.com/koranke/backend-playground/actions/workflows/build-and-test.yml/badge.svg)](https://github.com/koranke/backend-playground/actions/workflows/build-and-test.yml)

This project was derived from the Spring Guides project for accessing MySQL data with Spring Boot. The original project 
can be found [here](https://spring.io/guides/gs/accessing-data-mysql/).
It's been updated to include some additional features and to be used as a playground for testing exercises.  For example,
to practice unit tests, integration tests and, through a different project, system tests.
Finally, this project can also be used for testing CI/CD pipelines.

The project currently uses Java 17 with Maven for builds.  The service is a Spring Boot application that uses a MySQL
database for persistence. It includes Docker files for running the application and the database in containers.
This is not intended to be a production-ready application, but rather a simple example for testing purposes.
For example, it has no security, no logging, no environment configurations (for example, for PROD), etc.  These may be
added later.

## Running the Application
Ensure you have Docker installed on your machine.  You can download Docker Desktop from [here](https://www.docker.com/products/docker-desktop).
You will also need to set environment variables for your desired values for DB_USER, DB_ROOT_PASSWORD and DB_PASSWORD (for MySQL configuration).

To package the application, run the following command:
```shell
mvn clean package
```

To run the application in Docker containers, run the following command:
```shell
docker-compose --profile full up
```

