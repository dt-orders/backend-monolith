#!/bin/bash

clear

REPOSITORY=$1
VERSION_TAG=$2

if [ -z "$REPOSITORY" ]
then
    REPOSITORY=dtdemos
fi

if [ -z "$VERSION_TAG" ]
then
    VERSION_TAG=1
fi

IMAGE=$REPOSITORY/dt-orders-backend-monolith

#./mvnw clean package
echo "Compiling Java"
./writeManifest.sh
./mvnw clean package -Dmaven.test.skip=true

echo ""
docker build -t $IMAGE:1 . --build-arg APP_VERSION=1
docker build -t $IMAGE:2 . --build-arg APP_VERSION=2
docker build -t $IMAGE:3 . --build-arg APP_VERSION=3

docker tag $IMAGE:1 $IMAGE:1.0.0
docker tag $IMAGE:2 $IMAGE:2.0.0
docker tag $IMAGE:3 $IMAGE:3.0.0

echo ""
echo "========================================================"
echo "Ready to push images ?"
echo "========================================================"
read -rsp "Press ctrl-c to abort. Press any key to continue"

echo "Pushing $IMAGE:1"
docker push $IMAGE:1
docker push $IMAGE:1.0.0

echo "Pushing $IMAGE:2"
docker push $IMAGE:2
docker push $IMAGE:2.0.0

echo "Pushing $IMAGE:3"
docker push $IMAGE:3
docker push $IMAGE:3.0.0