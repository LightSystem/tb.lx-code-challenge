#!/bin/sh

set -m # Enable Job Control

for i in `seq 500`; do # start 500 jobs in parallel
  curl "http://localhost:8080/vehicles/list" \
  -X POST \
  -d "{  \"startTime\": \"2012-11-25T03:00:00\",  \"endTime\": \"2012-11-25T07:00:00\",  \"operator\": \"SL\"}" \
  -H "Content-Type: application/json" &
done

# Wait for all parallel jobs to finish
while [ 1 ]; do fg 2> /dev/null; [ $? == 1 ] && break; done