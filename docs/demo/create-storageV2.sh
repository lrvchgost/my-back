#!/bin/bash

UUID=$(uuidgen)

# 2. Выполняем запрос с правильными кавычками и экранированием
RESULT=$(curl -s -X POST localhost:8080/v2/storage/create \
  -H "Content-Type: application/json" \
  -d "{
    \"traceId\": \"$UUID\",
    \"requestType\": \"create\",
    \"debug\": {
      \"mode\": \"prod\"
    },
    \"storage\": {
      \"title\": \"Some storage\",
      \"description\": \"Хранилище для пользователей без почты\",
      \"capacity\": \"100\",
      \"availability\": \"99.99\",
      \"paymentType\": \"free\",
      \"readSpeed\": \"100\",
      \"writeSpeed\": \"100\",
      \"enableOptimize\": \"1\"
    }
  }")

echo $RESULT | jq