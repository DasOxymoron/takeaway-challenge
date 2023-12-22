# Takeaway coding challenge

## Starting the application locally
The application is depending on a running Postgres and RabbitMQ instance. I was using Docker to start both services locally. Please run `docker-compose up` to start both services. 

Please run `mvn spring-boot:run` to start the application locally. The application will be available under http://localhost:8080

Be aware that there is no configuration of the Rabbit-Queue within the Java Code. If you want to run the application against anything different than the local containers please set those up accordingly e.g. via Terraform. 
## Running the tests
Please run `mvn verify` to run the tests.

Be aware that the tests are running TestContainers under the hood, you will need to have a docker environment available. 

Tests needing a docker environment are Suffixed with `*IT.java`
## API Documentation
### REST API
I made a shortcut on the API documentation. 

Usually I would prefer APi-first by using Spring-Fox and automatically generated API clients - while the API itself is exposed as an interface and the developer has to implement the interface. 

Since we do not have such a project in this environment, I was using Spring-Docs to automatically generate API documentation which you can  access after starting the service under http://localhost:8080/swagger-ui/index.html

You can find the REST-API definition as well in the api-docs folder.
### Messaging API
Messaging API can be found in the api-docs/messaging folder as well. You can view the API specification within https://studio.asyncapi.com/. 

This API definition should be compliant for backstage.io documentation hub.
## Design Considerations