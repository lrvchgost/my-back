#!/bin/bash

STORAGE_ID1=$1
STORAGE_LOCK1=$2
STORAGE_ID2=$3
STORAGE_LOCK2=$4
UUID=$(uuidgen)

RESULT=$(curl -X POST localhost:8080/v2/storage/optimize \
  -H "Content-Type: application/json" \
  -d "{
    \"traceId\": \"$UUID\",
    \"requestType\": \"optimize\",
    \"debug\": {
      \"mode\": \"prod\",
      \"stub\": null
    },
    \"storages\": [
      {
        \"title\": \"Some storage\",
        \"description\": \"Хранилище для пользователей без почты\",
        \"capacity\": \"100\",
        \"availability\": \"99.99\",
        \"paymentType\": \"free\",
        \"readSpeed\": \"100\",
        \"writeSpeed\": \"100\",
        \"id\": \"$STORAGE_ID1\",
        \"lock\": \"$STORAGE_LOCK1\",
        \"enableOptimize\": \"1\"
      },
      {
        \"title\": \"Some storage\",
        \"description\": \"Хранилище для пользователей без почты\",
        \"capacity\": \"100\",
        \"availability\": \"99.99\",
        \"paymentType\": \"free\",
        \"readSpeed\": \"100\",
        \"writeSpeed\": \"100\",
        \"id\": \"$STORAGE_ID2\",
        \"lock\": \"$STORAGE_LOCK2\",
        \"enableOptimize\": \"1\"
      }
    ]
  }")

echo $RESULT | jq