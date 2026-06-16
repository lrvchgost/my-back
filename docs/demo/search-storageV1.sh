#!/bin/bash

SEARCH_STRING=$1
UUID=$(uuidgen)

RESULT=$(curl -X POST localhost:8080/v1/storage/search \
  -H "Content-Type: application/json" \
  -d "{
    \"traceId\": \"$UUID\",
    \"requestType\": \"search\",
    \"debug\": {
      \"mode\": \"prod\",
      \"stub\": null
    },
    \"searchFilter\": {
      \"searchString\": \"$SEARCH_STRING\"
    }
  }")

echo $RESULT | jq
