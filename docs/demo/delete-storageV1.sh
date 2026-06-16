#!/bin/bash

STORAGE_ID=$1
STORAGE_LOCK=$2
UUID=$(uuidgen)

RESULT=$(curl -X POST localhost:8080/v1/storage/delete \
  -H "Content-Type: application/json" \
  -d "{
    \"traceId\": \"$UUID\",
    \"requestType\": \"delete\",
    \"debug\": {
      \"mode\": \"prod\",
      \"stub\": null
    },
    \"storage\": {
      \"id\": \"$STORAGE_ID\",
      \"lock\": \"$STORAGE_LOCK\"
    }
  }")

echo $RESULT | jq
