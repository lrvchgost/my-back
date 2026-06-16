#!/bin/bash

STORAGE_ID=$1
STORAGE_LOCK=$2
UUID=$(uuidgen)

RESULT=$(curl -X POST localhost:8080/v2/storage/update \
  -H "Content-Type: application/json" \
  -d "{
    \"traceId\": \"$UUID\",
    \"requestType\": \"update\",
    \"debug\": {
      \"mode\": \"prod\",
      \"stub\": null
    },
    \"storage\": {
      \"id\": \"$STORAGE_ID\",
      \"title\": \"storage title new\",
      \"description\": \"storage description new\",
      \"paymentType\": \"free\",
      \"readSpeed\": \"100\",
      \"writeSpeed\": \"100\",
      \"lock\": \"$STORAGE_LOCK\",
      \"enableOptimize\": \"1\"
    }
  }")

echo $RESULT | jq
