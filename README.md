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

## Configuration
the following configurations are adjustable:
- 'scheduled.employees-clean-job': We need to delete users having an already ended working contract. This configuration property can be adjusted in order to define the granularity/ timing per job run
- 'scheduled.outbox': Configuration of the time interval for the outbox scheduler (see outbox pattern for more information)
- 'messaging.queue-name': Definition of the queue for publishing employee CDC events
- 'messaging.external-in.contract.queue': name of the queue in order to listen to contract ended events.
 

## Design Considerations and limitations
### Structure
- The application consists out of separated layers which are in separated packages. 
- API Package contains everything which goes into the service: REST Requests as well as Messaging.
- The domain layer is entirely independent of any other layer, all dependencies are pointing towards the domain layer.
- I made a shortcut from api to persistence layer for the last working day feature since I did not want to pollute the domain layer with this functionality. The domain layer is entirely unaware of this feature. 
### Considerations on API Behaviour
- I use Zalando-Problem-Web library for error handling. Therefor, any error suits a dedicated structure
- I am using Zalando logbook for logging rest requests in order to boost troubleshooting possibilities
- Deletion of users is idempotent - the external client will never get any feedback weather he passed a valid id or not
### Considerations on Scheduling Behaviour
- I am using several Scheduled Jobs. However I added a lock mechanism to prevent multiple executions of the same job in case of the service could be deployed having more than one instance on the AutoScalingGroup.
### Considerations on Testing
- I did not yet implement BDD Tests. Nevertheless I think they are crucial and if I have enough time this weekend I will add those for regression.
- Testing can be done entirely isolated. All you need is a docker environment, the rest should be pulled up from the test setup automatically. 
### Downstream messaging
- In order to guarantee at least once delivery, I am using RabbitMQ with a dedicated outbox pattern. 
- Events will be published asynchronously via a scheduled job. 
### Limitations
- I did not implement any security features. Usually I would use Spring-Security for this purpose but did not want to ship it with a dedicated auth server
- I did not add any @Queue beans for the RabbitMQ Listener since I expect creation of queues done in an external devops repository e.g. done by Terraform
- The application.yaml is designed for a local run. However you can change the configuration by adjusting the variables according to the spring properties chain.  