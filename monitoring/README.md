# Monitoring

Some monitoring has been added to the application utilising Prometheus, Loki and Grafana. Executing this requires docker to be running on your system.

## Using

1. Run using 
   `docker compose up`

2. 2 dashboards will then be available from a broweser
* The first shows metrics scraped from the spring boot application:
`http://localhost:3000/d/ad_shortener/shortener-dashboard`
* The second shows the logs of the spring boot application:
`http://localhost:3000/d/ad_shortener_logs/logs-dashboard`

> **_NOTE:_**  username is `admin` and password is `admin`
