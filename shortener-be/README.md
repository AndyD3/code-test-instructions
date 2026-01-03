# Back End for URL shortener

This in the back end which connects to the db which should be ran using docker compose in the root folder.

## Steps to run
1. Ensure maven is installed
2. Build the project and run unit tests using
   `mvn clean install`
3. Run using `mvn spring-boot:run`
4. The web application is accessible via http://localhost:8080 with JSON short urls available at http://localhost:8080/urls

## Running the automated tests

To run just the unit tests use:
```
mvn clean test
```
To run the integration tests (requires the database to be up and running - see root folder) use:
```
mvn failsafe:integration-test
```

## Manual Testing 

Although the supplied front end can be used to test the application curl commands can also be used.

To create a short url you can supply the full url in a command as such:

```
curl --request POST \
--url http://localhost:8080/shorten \
--header 'Content-Type: application/json' \
--data '{
"fullUrl" : "https://www.merriam-webster.com/dictionary/doggo"
}'
```

If you wish to have a custom alias then you add the customAlias property as such:
```
curl --request POST \
--url http://localhost:8080/shorten \
--header 'Content-Type: application/json' \
--data '{
"fullUrl" : "https://www.merriam-webster.com/dictionary/doggo",
"customAlias" : "my_alias"
}'
```

To utilise a short url you can just navigate to it in the browser, and you will redirected to the long url or you use the short url in a curl command as such:
```
curl --request GET \
--url http://localhost:8080/my_alias \
```

To retrieve all urls in JSON:
```
curl --request GET \
--url http://localhost:8080/urls \
```

To delete a url use the delete command, specifying the short url for example:
```
curl --request DELETE \
--url http://localhost:8080/my_alias \
```
