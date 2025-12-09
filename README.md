# URL Shortener Coding Task

This is in response to the code task for TPXImpact. 

# Instructions 
Spin up the database by utilising the docker compose file in this folder (this requires a docker environment of course)
```
docker compose up
```
Next, run the front end and back end from their folders (instructions are in the corresponding folders in the README files) 
The front-end requires npm installed and the backend requires maven.

# System Diagrams 

A possible system design

![Design diagram](./TPXImpact.drawio.svg)

# Considerations for scalability, resilience

Some brief points:

### The Count Service
*  The count functionality could go its own service with its own storage; allowing independent scaling.
*  Rather than returning a single count it could return a range of counts thereby reducing traffic.
* Clearly an entire database is overkill for storing a single field. A durable high speed cache would be better.

### Read and write services
* The back end service could split into 2 services, 1 for creation and 1 for retrieval; allowing independent scaling. This works well as the 2 traffic for the read would be considerably higher and if the write part of the system went down it wouldn't affect the more critical read aspect.

### Database
* The database was picked as no relational information is stored and easy to add extra data for example fields such as creation dates, amount used etc
* The database could be partitioned with the `shortUrl` key (consistent hashing) to distribute load evenly.
* The database could also be replicated to give greater resilience and reduced latency.
* You could add caching layer (with TTL/LRU) in front of the database due to high reads and hot lookups.

# Considerations for observability.
* Enable Spring Boot Actuator and instrument with OpenTelemetry. Export the metrics to Prometheus/Grafana and traces to a tracing backend.  
* Track business metrics: requests per short URL, redirect latency, error rates, cache hit ratio. 
* We may wish to track the number of hits each URL has, the 302 redirect means that the URL is not being cached and, whilst this creates more traffic, more accurately tracks the usage.

# Deploying to cloud
* To deploy this anywhere we need something like Kubernetes, which would allow us to describe the structure and desired state of the system, vital for robustness. Possibly in conjunction with cEKS
* To setup EKS (or whatever offering we go with) we should use some form of "infrastructure as code" option like Terraform
* Load balancers should be used in front of the services (see above) when multiple instances are required. 
* Cloud providers do provide durable caches such as the popular Redis which could be used for maintaining the count.
* An API gateway should be used for architectural goodness such as rate limiting, authentication/authorization, logging etc