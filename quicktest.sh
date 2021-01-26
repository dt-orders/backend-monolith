#!/bin/bash

URL=$1
PORT=$2

if [ -z "$URL" ]
then
    URL=http://localhost
fi

if [ -z "$PORT" ]
then
    PORT=8080
fi

export URL=$1
clear
if [ $# -lt 1 ]
then
  echo "missing arguments. Expect ./quicktest.sh <url> <port>"
  echo "example: ./quicktest.sh http://localhost 8080"
  exit 1
fi

export ENDPOINT=$URL:$PORT/order/line
echo "Calling $ENDPOINT..."
for i in {1..100}; 
do curl -s -I -X POST $URL/order/line | head -n 1 | cut -d$' ' -f2; 
done 

