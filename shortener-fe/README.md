# Front End for URL shortener

This in the front end which connects to the back end which should be ran from the shortner-be folder

This will: 
* Retrieve and display short urls from backend
* Allow creation of new short urls (using an alias or not) 
* Allow deletion of short urls by clicking the Delete button next to them
* Allow you to test short urls by clicking on them (will redirect to full url in new tab)

To create a short url you can:
* Supply only Full URL and submit, this will generate and assign a short url linking it to the Full URL
* Supply Full URL, Custom alias and submit, this will generate the short url (if the alias doesn't already exist) linking it to the Full URL

Either method will result in either a success or failure message under the submit button.

## Steps to run
1. Ensure npm is installed
2. Build the project using
   `npm install`
3. Run using `npm run dev`
4. The web application is accessible via http://localhost:5173/