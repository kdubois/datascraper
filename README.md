# datascraper project (WIP)

This Quarkus project scrapes a website (Bruvax) to see if a value in the content has changed, and in that case it sends a Telegram notification. 

There are 2 components:
    * A data scraper REST service (DataScraper.java)
    * A Camel Route (BruvaxBot.java) that consumes the datascraper website, compares with an existing value stored in a config map, and if the value has changed, it will send a Telegram notification 

# You will need to create a Telegram bot 
Go to https://core.telegram.org/bots#6-botfather and follow the instructions to create a Telegram Bot.  Add it to a group chat (or create one).  
You will need to retrieve the bot's token, and the chat id of the chat where you want the message to appear.  

# Store Telegram token and chat id 
Either store them directly in the application.properties file, or (preferably) store them in a Kubernetes secret.  To do this, add the values to a kubefiles/secrets.yml (there's a secrets-example.yml you can use as a template)

`kubectl apply -f kubefiles/configmap.yml kubefiles/secrets.yml kubefiles/rbac.yml -n datascraper`

# Build & deploy the application
Either deploy the application as a normal kubernetes deployment with
`./mvnw clean package -Dquarkus.kubernetes.deploy=true`

Or, you can deploy it as a native built serverless app with the following commands (replace the registry url with your own)
```shell script 
./mvnw package -Pnative -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=podman -Dquarkus.native.builder-image=registry.access.redhat.com/quarkus/mandrel-20-rhel8:20.3
podman build -f src/main/docker/Dockerfile.native -t quay.io/kevindubois/bruvax:latest .
podman push quay.io/kevindubois/bruvax:latest
kn service update bruvax --image=quay.io/kevindubois/bruvax
```


## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/datascraper-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html.




