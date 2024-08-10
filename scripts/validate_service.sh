#!/bin/bash

# 서비스가 정상적으로 시작되었는지 확인
SERVICE_URL="http://localhost:8080/health"

echo "Checking if the service is running at $SERVICE_URL..."

for i in {1..10}; do
  RESPONSE=$(curl --write-out "%{http_code}" --silent --output /dev/null "$SERVICE_URL")
  if [ "$RESPONSE" -eq 200 ]; then
    echo "Service is up and running."
    exit 0
  else
    echo "Service not available yet (HTTP $RESPONSE). Waiting..."
    sleep 5
  fi
done

echo "Service did not start within the expected time."
exit 1
