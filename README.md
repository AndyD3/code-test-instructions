# URL Shortener Coding Task

This is in response to the code task for TPXImpact. 

# Instructions 
Spin up the database by utilising the docker compose file in this root folder (this requires a docker environment of course)
```
docker compose -f mongo.yml up
```
Next, run the front end and back end from their folders (instructions are in the corresponding folders in the README files) 
The front-end requires npm installed and the backend requires maven.

In addition you can also run monitoring on the application.  This also has its corresponding `monitoring` folder with the instructions inside its README.

# System Diagrams 

A possible system design

![Design diagram](./TPXImpact2.drawio.svg)

# Considerations for scalability, resilience

Some brief points:

### Containerisation
* For speed of development mongo autoconfiguration has been used, this would have to be changed in order to expand the containerisation for a 
fully fleshed out system using docker compose and kubernetes

### Seperate Read and write services
* As there would probably be much greater traffic on the read aspect we could split the service into 2; one for reading and one for reading. This would allow independent scaling. If the database is scaled in scaled into primary and secondary then the write would communicate with the primary and the read with any of the secondaries.

### Database
* The database was picked as no relational information is stored, no complex queries will be used and it's easy to add extra fields (for example user, creation date, access count)
* The database could be partitioned with the `shortUrl` key (consistent hashing) to distribute load evenly.
* The database could also be replicated to give greater resilience and reduced latency.
* A caching layer (with TTL/LRU) could be added in front of the database due to high reads and hot lookups.

# Considerations for observability.
* Improve the dashboards on grafana, specifically tracking business metrics: requests per short URL, cache hit ratio and add alerting for warnings and errors.
* We may wish to track the number of hits each URL has, the 302 redirect means that the URL is not being cached and, whilst this creates more traffic, more accurately tracks the usage.

# Deploying to cloud
* To deploy this anywhere we need something like Kubernetes, which would allow us to describe the structure and desired state of the system, vital for robustness. Possibly in conjunction with EKS
* To setup EKS (or whatever offering we go with) we should use some form of "infrastructure as code" option like Terraform
* Load balancers should be used in front of the services (see above) when multiple instances are required. 
* An API gateway should be used for architectural goodness such as rate limiting, authentication/authorization etc