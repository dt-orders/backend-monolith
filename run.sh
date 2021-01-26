#!/bin/bash

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

docker run -it -p 8080:8080 $FULLIMAGE 