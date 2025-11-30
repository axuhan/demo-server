#!/bin/bash

echo "waiting healthcheck..."
sleep 30

for times in {1..10}
do
  sleep 10
  echo "healthcheck ${times} times..."
  healthCheckResult=$(curl -s -S http://localhost:8082/healthcheck)
  if [ "${healthCheckResult}" = "OK" ]; then
    echo "healthcheck OK"
    exit 0
  fi
done

echo "healthcheck fail"
exit 1
