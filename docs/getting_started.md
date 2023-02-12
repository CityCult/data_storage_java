# Getting Started

* compile and build JAR package: `mvn clean verify`
* install to local Maven cache to be available for web service: `mvn install`
* to run integration tests successfully on developer workstation:
  * start a local *Data Storage* via Docker Compose in `docker/`
  * adjust `javax.persistence.jdbc.url` in `src/main/resources/META-INF/persistence.xml` to `localhost`
