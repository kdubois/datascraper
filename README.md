# Bruvax datascraper project 

This Camel Quarkus project scrapes a website (Bruvax) to see if a value in the content has changed, and in that case it sends a Telegram notification. 

There are 2 components:
    * A data scraper bean and service (DataScraper.java)
    * A Camel Route (BruvaxBot.java) that consumes the datascraper website, compares with an existing value stored in a config map, and if the value has changed, it will send a Telegram notification 

# You will need to create a Telegram bot 

Go to https://core.telegram.org/bots#6-botfather and follow the instructions to create a Telegram Bot.  Add it to a group chat (or create one).  
You will need to retrieve the bot's token, and the chat id of the chat where you want the message to appear.  

# Store Telegram token and chat id 

Either store them directly in the application.properties file, or (preferably) store them in a Kubernetes secret.  To do this, add the values to a kubefiles/secrets.yml (there's a secrets-example.yml you can use as a template)

`kubectl apply -f kubefiles/configmap.yaml -f kubefiles/secrets.yaml`

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```
> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

# Build & deploy the application

If you're running Openshift, you can build the app as a native binary trigger a knative serverless deployment easy-peasy with the following maven command:
`./mvnw clean package -Pnative -Dquarkus.kubernetes.deploy=true`
(make sure you have installed the Openshift Serverless operator!)

The camel route will run once and then scale down to 0, so if you want to trigger it on a regular interval, you could add a Pingsource to the app:

`kubectl apply -f kubefiles/pingsource.yaml`
