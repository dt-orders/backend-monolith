#!/bin/bash

FULLIMAGE=dtdemos/dt-orders-frontend:1

docker run -it -p 80:8080 \
  --env CUSTOMER_URL=http://172.17.0.1:8080/customer \
  --env CATALOG_URL=http://172.17.0.1:8080/catalog \
  --env ORDER_URL=http://172.17.0.1:8080/order \
  $FULLIMAGE 