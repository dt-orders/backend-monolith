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

IMAGE=dt-orders-backend-monolith
FULLIMAGE=$REPOSITORY/$IMAGE:$VERSION_TAG

echo ""
echo "========================================================"
echo "Building $FULLIMAGE"
echo "========================================================"

./writeManifest.sh

#./mvnw clean package
./mvnw clean package -Dmaven.test.skip=true
docker build -t $FULLIMAGE .

echo ""
echo "========================================================"
echo "Ready to push $FULLIMAGE ?"
echo "========================================================"
read -rsp "Press ctrl-c to abort. Press any key to continue"

docker push $FULLIMAGE