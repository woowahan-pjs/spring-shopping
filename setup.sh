#!/bin/bash
set -e

BASE_URL="http://localhost:8080"

#echo "=== Starting Docker Compose ==="
#docker compose up -d
#echo "Waiting for MySQL to be ready..."
#until docker compose exec mysql mysqladmin ping -ushopping -pshopping --silent 2>/dev/null; do
#  sleep 1
#done
#echo "MySQL is ready."
#
#echo ""
#echo "=== Starting Spring Boot ==="
#./gradlew bootRun &
#APP_PID=$!
#
#echo "Waiting for app to start..."
#until curl -s -o /dev/null -w "" "${BASE_URL}/health/liveness" 2>/dev/null; do
#  sleep 1
#done
#echo "App is running."

#echo ""
#echo "=== Registering Members ==="
#curl -s -X POST "${BASE_URL}/api/members/register" \
#  -H "Content-Type: application/json" \
#  -d '{"email":"user@test.com","password":"password1234"}' | jq .
#
#curl -s -X POST "${BASE_URL}/api/members/register" \
#  -H "Content-Type: application/json" \
#  -d '{"email":"admin@test.com","password":"password1234"}' | jq .

echo ""
echo "=== Logging in (user@test.com) ==="
TOKEN=$(curl -s -X POST "${BASE_URL}/api/members/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"user@test.com","password":"password1234"}' | jq -r '.token')
echo "Token: ${TOKEN:0:20}..."

echo ""
echo "=== Creating Products ==="
curl -s -X POST "${BASE_URL}/api/products" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${TOKEN}" \
  -d '{"name":"test1s","price":89000,"imageUrl":"https://picsum.photos/seed/keyboard/400/300"}' | jq .

#curl -s -X POST "${BASE_URL}/api/products" \
#  -H "Content-Type: application/json" \
#  -H "Authorization: Bearer ${TOKEN}" \
#  -d '{"name":"Mouse","price":45000,"imageUrl":"https://picsum.photos/seed/mouse/400/300"}' | jq .
#
#curl -s -X POST "${BASE_URL}/api/products" \
#  -H "Content-Type: application/json" \
#  -H "Authorization: Bearer ${TOKEN}" \
#  -d '{"name":"Monitor","price":350000,"imageUrl":"https://picsum.photos/seed/monitor/400/300"}' | jq .
#
#curl -s -X POST "${BASE_URL}/api/products" \
#  -H "Content-Type: application/json" \
#  -H "Authorization: Bearer ${TOKEN}" \
#  -d '{"name":"Headset","price":120000,"imageUrl":"https://picsum.photos/seed/headset/400/300"}' | jq .
#
#curl -s -X POST "${BASE_URL}/api/products" \
#  -H "Content-Type: application/json" \
#  -H "Authorization: Bearer ${TOKEN}" \
#  -d '{"name":"Webcam","price":75000,"imageUrl":"https://picsum.photos/seed/webcam/400/300"}' | jq .

echo ""
echo "=== Setup Complete ==="
echo "Homepage:       ${BASE_URL}/"
echo "Login page:     ${BASE_URL}/login"
echo "Test accounts:  user@test.com / password1234"
echo "                admin@test.com / password1234"
echo ""
echo "Press Ctrl+C to stop the app."
wait $APP_PID
