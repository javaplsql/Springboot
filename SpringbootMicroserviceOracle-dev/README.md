# plsql Spring Boot fcm-app-accountmanaging Function

## Features

### Description

Microservice fcm-app-accountmanaging generated from the Maven archetype for plsql Spring Boot applications.
The microservice fcm-app-accountmanaging implements an application of type Servlet, which exposes a handler, and basic endpoints to process GET requests. 
The application follows Hexagonal Architecture design principles, which aims to separate the core business logic from external dependecies and technologies. 
The microservice archetype was adapated to hexagonal architecture by Mexico´s DCoE (Development Center of Excellence). 
This architecture is composed of three main layers: Domain, Application and Infrastructure. The Domain layer contains the core business objects and logic of the application, the Application layer facilitates controlled interaction with the domain, and the Infrastructure layer isolates the application´s core from external technology or framework-specific code. This architecture ensures that the core business logic remains reusable and adaptable across different environments.

The microservice fcm-app-accountmanaging, makes use of plsql Spring Boot Starter Parent, so there is no need to worry about the version of the libraries it makes use of, or to provide a list of all available dependencies.

!!! note

    For more information on how to generate a microservice from the plsql archetype, please refer to the following link: [plsql Spring Boot Archetype Microservice](https://atom.dev.corp/microservices/docs/plsql-spring-boot/current/plsql-archetypes/plsql-spring-boot-archetype-microservice/index.html)

### Dependency management

The microservice fcm-app-accountmanaging makes use of the following Maven dependencies:

* [Spring Cloud Config Client](https://cloud.spring.io/spring-cloud-config/reference/html/#_spring_cloud_config_client)
* [Spring Web MVC](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html)
* [Springdoc-OpenAPI (Swagger)](https://springdoc.org/)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
* Spring HTTP Clients: [RestTemplate y WebClient](https://github.com/java-group-shared-assets/gln-back-plsql-java-samples/tree/develop/webclient)
* [Oracle](https://www.oracle.com/database/)
* [H2 Database](https://h2database.com/html/main.html)

* Framework plsql Spring Boot:
- [plsql Core](https://atom.dev.corp/microservices/docs/plsql-spring-boot/current/plsql-project/plsql-spring-boot-core/)
- [plsql Cache](https://atom.dev.corp/microservices/docs/plsql-spring-boot/current/plsql-project/plsql-spring-boot-cache/)
- [plsql Logging](https://atom.dev.corp/microservices/docs/plsql-spring-boot/current/plsql-project/plsql-spring-boot-logging/)
- [plsql Authentication](https://atom.dev.corp/microservices/docs/plsql-spring-boot/current/plsql-project/plsql-spring-boot-security-authentication/)
- [plsql OmniChannel](https://atom.dev.corp/microservices/docs/plsql-spring-boot/current/plsql-project/plsql-spring-boot-omnichannel/)

## Project structure

The microservice fcm-app-accountmanaging has the following structure:

The `src/main/java` folder includes :

- A `domain` package, which contains the core business objects and logic of the application. It can include two additional inner packages, `entity` and `service`, depending on the generated example application.

    - `entity` package: This package contains the domain entities or objects that represent the core business concepts of your application. These entities encapsulate the state and behavior of the domain and are independent of any specific framework or technology. They should contain the essential attributes and methods that define the behavior and rules of the domain.

    - `service` package: This package contains the domain services that encapsulate the business logic and orchestrate the interactions between the domain entities. These services implement the core operations and workflows of your application. They should be focused on the business rules and should not be concerned with the technical details or infrastructure. The services should interact with the domain entities and other services to fulfill the requirements of the use cases.

- A `application` package, which facilitates controlled interaction with the domain, allowing external systems to utilize the core business logic without exposing implementation details. It comes with two additional inner packages, `ports` and `usecases`. 

    - `ports` folder:
        - `input` package: Contains input ports that implement the use case interfaces. These ports are intended for input adapters, and allow interaction with the core of the application. They handle incoming requests.
        - `output` package: Contains interfaces that define the behavior for the application's interaction with external sources (e.g. databases). They specify the methods needed from external systems, which are implemented by output adapters.

    - `usecases` folder: Define interfaces representing the main actions or workflows the application must perform. Each interface represents a specific use case, describing the methods needed to achieve business goals.

 - A `infrastructure` package, that isolates the application´s core from external technology or framework-specific code. By keeping these dependecies separate, it ensures the core business logic remains reusable and adaptable across different environments. Contains:

    -`adapters` folder:
        -`input`: Contains classes like controllers that allow users or systems to interact with the application. These classes use input ports to perform necessary operations.
        -`output`: Implements output ports to enable the application to interact with external resources (e.g., databases, external APIs). Contains the repository classes, framework-specific entity classes and mappers to transform objects from the domain to these entities.

    -`config` folder: Infrastructure also contains a config folder for framework-specific configuration.  It includes **ApplicationConfiguration.java** for spring related configuration and **SwaggerConfig.java** that implements configuration for using the OpenApi documentation for rest applications.    

In addition, the microservice has:

- An **application.yml** file together with an *application-local.properties* file with the [properties](#configuration-files).
  necessary to configure the project.
- An **errors.properties** file for the [configuration of exceptions](https://atom.dev.corp/microservices/docs/plsql-spring-boot/current/plsql-project/plsql-spring-boot-core/index.html#excepciones), and a subfolder errors with a Resource Bundle of configurations, for multi-language exceptions (in this case: Spanish (es_ES) and US English (en_US)).
- The **pom.xml** file, which contains all the information that Maven uses to manage dependencies, metadata, etc.
- An **ApplicationTest.java** to test that the functionality of the application works correctly. 
Also the corresponding tests for generated classes. Input ports, input adapters, output adapters include their respective units test in the src/test/java folder, An **application-local.properties** for testing with H2 Database has also been added.


## Configuration files

The project has a number of property files to configure what is needed in relation to the libraries used:

### _application.yml_

Main configuration file of the environment-independent application.
In this file, properties related to the architecture components and those of the application itself are configured.

This file contains the following properties of architecture components:

| Property                                                | Description                                                                                                                                                                                                                             | Required                          | Value                                       | Environment |
|---------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------|---------------------------------------------|-------------|
| *plsql.app-key*                                        | Indicates the application key.Within the monitoring system it will be part of the index name.                                                                                                                                           | Yes                               | _acronym-app_                               | Any         |
| *plsql.logging.paas-app-version*                       | Artifact version. To have it automatically populated when generating the artefact, we must indicate "@project.version@" as the value and tell Maven to process the resources so that it replaces the string with the corresponding value. | Yes                               | @project.version@                           | Any         |
| *plsql.logging.entity*                                 | Indicates the country.                                                                                                                                                                                                                  | No                                | ESP                                         | Any         |
| *plsql.core.exceptions.error-format*                   | Configures the microservice's error model to be plsql format or atom format                                                                                                                                                           | No                                | atom                                       | Any         |
| *plsql.security.connectors.pkm-connector.pkm-endpoint* | Public key manager endpoint                                                                                                                                                                                                             | Yes                               | ${env.pkm-endpoint}                         | Any         |
| *spring.session.store-type*                             | Determines where Spring Session Saving is implemented. Defaults to the classPath if only one module is present there. Set to none, disables Spring Session.                                                                             | Yes                               | none                                        | Any     |    
| *spring.cache.type* | Configure the type of cache. | Yes | caffeine | Any |
| *spring.cache.caffeine.spec*                            | Cache settings.                                                                                                                                                                                                                         | Yes                               | expireAfterWrite=10m                        | Any         |
| *logging.level.com.java.fcm.app.fcmappaccountmanaging*                              | Configure the level of detail of the logs in com.java.fcm.app.fcmappaccountmanaging                                                                                                                                                                                 | Yes                               | INFO                                        | Any         |
| *logging.level.root*                                    | Configure the level of detail of the logs at root level.                                                                                                                                                                                | Yes                               | ERROR                                       | Any         |
| *management.endpoint.health.show-details*               | Determines to whom the -health endpoint details are shown. Configured 'when-authorized' only shows the details to authorized users, which can be configured by using the _management.endpoint.health.roles_.                            | Yes                               | ALWAYS                                      | Any         | 
| *health.config.enabled*                                 | Enable the health indicator.                                                                                                                                                                                                            | Yes                               | false                                       | Any         |
| *springdoc.swagger-ui.disable-swagger-default-url*      | Disable the default openApi url, so that the documentation can only be accessed via the custom path.                                                                                                                                    | Yes                               | true                                        | Any         |
| *springdoc.swagger-ui.path*                             | Customize the Swagger documentation path in HTML format.                                                                                                                                                                                | Yes                               | /swagger-ui.html                            | Any         | 
| *server.max-http-request-header-size*                   | Allows to set the maximum HTTP header size to desired value.                                                                                                                                                                              | No                                                                                                                                                          | 128KB                                       | Any         |
| *server.forward-headers-strategy*                       | Manages the use of proxy variables.                                                                                                                                                                                                     | Yes                               | framework                                   | Any         |
| *server.shutdown*                                       | Shutdown mode for embedded web servers (Tomcat, Jetty, Undertow and Netty), on both servlet and reactive platforms.                                                                                                                     | No                                | graceful                                    | Any         |
| *spring.lifecycle.timeout-per-shutdown-phase*           | Active request shutdown grace time, if this property is not defined, a default value of 30 seconds will be applied.                                                                                                                     | No                                | 2m                                          | Any         | 

Among others, this file allows the definition of properties to be able to identify the application. By default, it points to the "boae" *region* and the active profile is the *local* one. To identify the application in the monitoring systems too, *entity* property are set by default with "ESP" value.

	plsql:
	    region: boae
	    suffix:
	    logging:
            entity: ESP
	...
	
	spring:
	    application:
            name: application-1
	    profiles:
            active: local
	...

### _application-local.properties_

Auxiliary file with properties associated with the environment (in this case for a local environment).
It has a property called `plsql.logging.console-log-format` that allows to change the technical console log format.
By default is set to the value 'HUMAN_READABLE' to show the Console Technical Log Pattern but it can be changed to 'JSON' to show the Technical Log Pattern instead.
It has the PKM and STS properties of the security library.
In case of working on another profile, it will be necessary to create another properties file for the particular profile.

### Database Configuration

In the **application-local.properties** the different properties for the configuration of the Database can be found under the `env.database` properties.
It will be necessary to modify the env.database.url to point to the Database to be used. Also, the username and the password are needed to correctly connect to the Database.

    # Configuration related to Databases
    env.database.url: (...)/replace-by-correct-database-name
    env.database.username: username
    env.database.password: password

Apart from the main configuration, it can be found several optional properties to configure debugging when working with the Database. If not needed, these can be safely removed.

    # Show SQL queries on log
    spring.jpa.show-sql: true
    spring.jpa.properties.hibernate.format-sql: true
    # Register parameter values on log
    spring.jpa.properties.hibernate.use_sql_comments: true
    spring.joa.properties.hibernate.type: trace
    # Configure Hibernate logs
    logging.level.org.hibernate.SQL: DEBUG
    logging.level.org.hibernate.type.descriptor.sql.BasicBinder: TRACE

In the **application.yml** file, it is possible to find configuration related to the connection pool. These are the default value, and they can be modified if needed:

    hikari:
      minimum-idle: 1
      maximum-pool-size: 2
      idle-timeout: 36000
      max-lifetime: 1800000
      connection-timeout: 20000
      leak-detection-threshold: 3000

### How to Auto Generate Repositories and Entities

In the pom.xml, the plugin `Arsenal JPA Code Generator` is a code generator that parses Data Definition Language commands from SQL and translates them into JPA Java classes.

This plugin has been included to simplify the development of the entities, repositories and their respective test files along with a `schema` file that includes 
the SQL sentences for the classes to be automatically generated. It is important to notice that this plugin will only support `CREATE TABLE` statements.

An example of a file:

    CREATE TABLE IF NOT EXISTS table_example
    (
    id int NOT NULL,
    title TEXT NON NULL,
    state BOOLEAN NULL,
    date TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
    );

To configure the plugin, it is also necessary to point to the target packages where the classes will be created and to specify the file where the SQL sentences are stored.

    <!-- Arsenal JPA Code Generator for Database Entities and Repositories -->
			<plugin>
				<groupId>com.java.ars</groupId>
				<artifactId>gln-back-arsenal-jpa-codegen-maven-plugin</artifactId>
				<executions>
					<execution>
						<phase>generate-sources</phase>
						<goals>
							<goal>generate</goal>
						</goals>
					</execution>
				</executions>
				<configuration>
					<sqlScript>${project.basedir}\src\main\resources\schema.sql</sqlScript>          <!--(1)-->
					<entityPackage>com.java.fcm.app.fcmappaccountmanaging.entity</entityPackage>                                 <!--(2)-->
					<repositoryPackage>com.java.fcm.app.fcmappaccountmanaging.repository</repositoryPackage>                     <!--(3)-->
					<fileOverride>false</fileOverride> <!-- This property is false by default -->
				</configuration>
			</plugin>

1. This is the location of the file with the SQL sentences to execute
2. The destination package for the entities that will be generated
3. The destination package of the repositories that will be generated

To execute this plugin, run the following command:

    mvn generate-sources

If you need more information, you can find everything you need about this plugin [here](https://atom.gs.corp/community/docs/latest/capabilities/arsenal/arsenal-backend/tutorials/code-generator-jpa-maven-plugin/)


## Spring Actuator: Liveness and Readiness.

Spring's actuators provide a set of HTTP/JMX endpoints that expose operational information about our microservice.
plsql makes use of the '/actuator/health/liveness' and '/actuator/health/readiness' actuator endpoints to manage the Liveness and Readiness probes of a microservice.

- Liveness probe: Provides information that lets us know if the microservice is alive or dead.
- Readiness probe: Provides information that lets us know if the microservice is ready to receive traffic.

These endpoints, by default, will only be enabled when the execution environment is detected to be Kubernetes as they are the endpoints we will use to define the health checks of the container.

- Liveness health checks: Kubernetes uses this health check to know if the application is alive or dead.
  So if the application is alive Kubernetes does nothing but if it is dead it deletes the Pod and starts a new one to replace it.
- Readiness health checks: Kubernetes uses this health check to know if the microservice is ready to receive traffic.
  Kubernetes makes sure that the microservice is ready to receive requests before the pod accepts them.
  If the healthcheck starts to fail, Kubernetes stops routing requests to the pod until the microservice is in a state that allows it to receive them.

In the [application.yml](#application.yml) , the properties that are applied by default are:

    management:
	    health:
		defaults.enabled: true
		livenessState.enabled: true
		readinessState.enabled: true

!!! note

    Does not need to be explicitly added* in the file of the microservice.

If you want to use the endpoints in a local environment, Spring provides the property **_management.health.probes.enabled_** that you have to add to the _application.yml_ file.

An example of call and response of the liveness probe, would be the following.
Using the endpoint 'http://localhost:8080/actuator/health/liveness' we would get a response indicating the internal status of the application.
When the status is OK, we get a response with HTTP=200 code and content:

	// HTTP/1.1 200 OK
	
	{
	"status": "UP",
	    "components": {
		"livenessProbe": {
		    "status": "UP"
		}
      }
	}


Similarly, to obtain the readiness probe, we use the endpoint 'http://localhost:8080/actuator/health/readiness'.
In this case we show an example that indicates that the application is not ready to receive requests:

	// HTTP/1.1 503 SERVICE UNAVAILABLE
	
	{
	"status": "OUT_OF_SERVICE",
	    "components": {
		"readinessProbe": {
		    "status": "OUT_OF_SERVICE"
		}
        }
	}

For more information on Liveness and Readiness, please refer to the following entries: [Spring Liveness and Readiness Probes](https://spring.io/blog/2020/03/25/liveness-and-readiness-probes-with-spring-boot) and [Health Check Microservices Java](https://sanes.atlassian.net/wiki/spaces/SANACLOUD/pages/16546334525/Health+Check)

## Execution of the application

To execute our function application we go to the `Application` class, where the main method is located.

	@SpringBootApplication
	@EnableCaching 
        
	public class Application {
	
	public static void main(String[] args) {
			
		  new SpringApplicationBuilder(Application.class).web(WebApplicationType.SERVLET).run(args);}

This class has the tags:

* `@SpringBootApplication` which indicates that it is a Spring boot application and causes it to activate.
  the Scan component and the autoconfigurations.
* `@EnableCaching` which enables the use of the cache in the application, managed by the plsql-spring-boot-cache library.
* `@Slf4j` generates a logger, and then the plsql-spring-boot-logging library connects to it.

To run the application, we go to the project directory and execute the following command:

    mvn spring-boot:run

Also, it is possible to run the application using the test profile with the 'ApplicationTestRun' class and the following command

    mvn spring-boot:test-run

When you run it, you will see the following log:


    :: Spring Boot  (v2.6.11.RELEASE) ::                                                                  :: plsql (v3.1.2-RELEASE) ::

    2021-07-08 12:59:08.558  INFO 3836 --- [           main] e.s.m.a.Application                      : The following profiles are active: local
    2021-07-08 12:59:26.590  INFO 3836 --- [           main] e.s.m.a.Application                      : Started Application in 22.826 seconds (JVM running for 24.724)

and the application would be up.

The microservice has a controller, which is the highest level layer for exposing our REST microservice.
This is defined in the `HelloControler` class.
To call the controller we use the `/fcm-app-accountmanaging/hello` endpoint indicated in the `@RequestMapping` tag.

By executing the following command:

    curl -X GET "http://localhost:8080/fcm-app-accountmanaging/hello"

We will get a response like this:

    Hello world!

Or an incorrect one in case of no authorization.

	{
		"timeStamp": "2020-07-08T11:08:05.577+00:00",
		"appName": "aplicacion-2",
		"status": 401,
		"errorName": "Unauthorized",
		"internalCode": 401,
		"shortMessage": "Unauthorized",
		"detailedMessage": "Not Authenticated",
		"mapExtendedMessage": {}
	}

To call the different endpoints related with the Database implemented, it is possible to use `DatabaseController` class.
To call the controller for adding an User we use the `/fcm-app-accountmanaging/databases/create-user/{name}/{mail}` endpoint indicated in the `@RequestMapping` tag, modifying the name and mail strings

By executing the following command:

    curl -X GET "http://localhost:8080/fcm-app-accountmanaging/databases/create-user/my-name/my-mail"

We will get a response like this:

    {"id":3,"userName":"my-name","email":"my-mail"}

To call the controller for retrieving an User we use the `/fcm-app-accountmanaging/databases/retrieve-user/{id}` endpoint indicated in the `@RequestMapping` tag, modifying the id by the corresponding Id

By executing the following command:

    curl -X GET "http://localhost:8080/fcm-app-accountmanaging/databases/retrieve-user/3"

We will get a response like this:

    {"id":3,"userName":"my-name","email":"my-mail"}


## Testing the application

The fcm-app-accountmanaging microservice has a series of tests included in the src/test/java folder:

* **ApplicationTest** : Checks that the Spring context, is loaded correctly.


* **SwaggerConfigTest** : Tests that the application has the correct OpenApi configuration.

* **HelloWorldServiceTest**: Tests the domain layer hello world service

* **HelloWorldInputPortTest**: Tests input port, verifying implementation is correct.

* **HelloControllerTest**: Tests the controller, verifying that the result obtained after a call to the endpoint is correct.

* **UserManagementInputPortTest**: Tests input port for database example, verifying implementation is correct.

* **DatabaseControllerITTest**: Tests the controller, verifying that the result obtained after a call to the endpoint is correct.

* **UserManagementJPAAdapterTest**: Tests the adapter class, verifying that implementation is correct.

* **UserJPAMapperTest**:  Tests the mapper that converts the entity to the domain object and vice versa.

* **UserDataRepositoryTest** : Tests the repository, verifying that the result obtained when reaching the Database is correct.

To test the application, from the project directory, run the following command in the terminal:

    λ mvn clean test


!!! note

    By default, the test scope is configured with the H2 database. It is also possible to perform the test in the local scope using ´spring-boot:test-run´ and the H2 database

The following result was obtained:

    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------

## Documenting the application's REST APIs

The microservice exposes a swagger interface with the documented API.

The API documentation in Swagger format, is exposed through the url: http://localhost:8080/swagger-ui.html

On the other hand, to consult the API documentation in yml format, access the endpoint: http://localhost:8080/v3/api-docs

!!! note

    For more information about the OpenApi documentation, see the following link: [API documentation with Springdoc](https://github.com/java-group-shared-assets/gln-back-plsql-java-samples/tree/develop/openapi-springdoc)

## Support

 - In case you detect any issue associated with the plsql framework, please open an issue through the support channel: [Report Issue](https://github.com/java-group-atom/gln-adoption-entities/issues)
 - In case you detect any issue on a Spain based project, please sign up on this support channel and open an issue: [Report Issue - Spain based project](https://sanes.atlassian.net/jira/software/c/projects/ESPARQSOP/boards/480)