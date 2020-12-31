# Overview

This repo has the code for the orders service for demostrations.  See the [overview](https://github.com/dt-orders/overview) repo for an overiew for that whole application.

# Developer Notes

For the order service to run locally, the ports and IP of these services needs to be passed into Docker as environment variables -AND- catalog, and customer services must be listening on the ports defined in this script.

## Pre-requisites

The following programs to be installed
* Java 14
* Docker

## Build and Run Locally

1. run these commands
  ```
  ./mvnw clean package
  java -jar target/*.jar
  ```
2. access application at ```http://localhost:8080```

## quicktest

Use the provided Unix shell script that loops and calls the app URL.  Just call:

```./quicktest.sh <catalog base url>```

For example:

```./quicktest.sh http://localhost:8080```

## Build Docker Images and push images to a repository

Use the provided Unix shell scipt that will build the docker image and publish it. 

    Just call: `./buildpush.sh <REPOSITORY> <VERSION_TAG>`

    For example: `./buildpush.sh dtdemos 1`

## Run Docker Image Locally

1. Here is an example of running version 1
  ```
  docker run -p 8080:8080 dtdemos/dt-orders-backend-monolith:1
  ```

2. access application at ```http://localhost:8080```

# Credits

* Orginal demo code: https://github.com/ewolff/microservice-kubernetes

# Install Java on Mac

Taken from this [blog](https://mkyong.com/java/how-to-install-java-on-mac-osx)

```
# install java
brew update
brew tap adoptopenjdk/openjdk
brew install --cask adoptopenjdk14
java -version

# edit profile
vi ~/.bash_profile
export JAVA_HOME_14=$(/usr/libexec/java_home -v14)
export JAVA_HOME=$JAVA_HOME_14

# reload profile
source ~/.bash_profile

# verify
echo $JAVA_HOME
```