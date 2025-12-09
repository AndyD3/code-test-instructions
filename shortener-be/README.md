# Back End for URL shortener

This in the back end which connects to the db which should be ran using docker compose in the root folder.

## Steps to run
1. Ensure maven is installed
2. Build the project using
   `mvn clean install`
3. Run using `mvn spring-boot:run`
4. The web application is accessible via http://localhost:8080 with JSON short urls available at http://localhost:8080/shortener/urls
