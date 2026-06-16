#!/bin/bash

STORAGE_ID=$1
UUID=$(uuidgen)

RESULT=$(curl -X POST localhost:8080/v1/storage/read \
  -H "Content-Type: application/json" \
  -d "{
    \"traceId\": \"$UUID\",
    \"requestType\": \"read\",
    \"storage\": {
      \"id\": \"$STORAGE_ID\"
    }
  }")

echo $RESULT | jq