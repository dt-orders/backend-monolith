#!/bin/bash

export URL=$1
clear
if [ $# -lt 1 ]
then
  echo "missing arguments. Expect ./quicktest.sh <order url>"
  echo "example: http://localhost:8000"
  exit 1
fi

export ENDPOINT=$URL/order/line
echo "Calling $ENDPOINT..."
for i in {1..100}; 
do curl -s -I -X POST $URL/order/line | head -n 1 | cut -d$' ' -f2; 
done 

