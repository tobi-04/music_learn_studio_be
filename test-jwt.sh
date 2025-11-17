#!/bin/bash

# Test JWT Authentication Implementation

echo "🧪 Testing JWT Authentication Implementation"
echo "=============================================="
echo ""

# Wait for server to be ready
echo "⏳ Waiting for server to start..."
sleep 5

BASE_URL="http://localhost:6888/api/v1"

# Test 1: User Registration
echo "📝 Test 1: User Registration"
echo "----------------------------"
REGISTER_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "username": "testuser123",
    "email": "testuser@example.com",
    "password": "Test@Password123",
    "avatar": "https://example.com/avatar.jpg"
  }')

echo "Response:"
echo "$REGISTER_RESPONSE" | jq '.' 2>/dev/null || echo "$REGISTER_RESPONSE"
echo ""

# Extract token
TOKEN=$(echo "$REGISTER_RESPONSE" | jq -r '.data.token' 2>/dev/null)

if [ ! -z "$TOKEN" ] && [ "$TOKEN" != "null" ]; then
  echo "✅ Token generated successfully!"
  echo "Token: ${TOKEN:0:50}..."
  echo ""
  
  # Test 2: User Login
  echo "🔐 Test 2: User Login"
  echo "---------------------"
  LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
    -H "Content-Type: application/json" \
    -d '{
      "usernameOrEmail": "testuser123",
      "password": "Test@Password123"
    }')
  
  echo "Response:"
  echo "$LOGIN_RESPONSE" | jq '.' 2>/dev/null || echo "$LOGIN_RESPONSE"
  echo ""
  
  LOGIN_TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.data.token' 2>/dev/null)
  
  if [ ! -z "$LOGIN_TOKEN" ] && [ "$LOGIN_TOKEN" != "null" ]; then
    echo "✅ Login token generated successfully!"
    echo ""
    echo "🔍 Token Verification (at jwt.io)"
    echo "---------------------------------"
    echo "Copy the following token to https://jwt.io to verify:"
    echo "$LOGIN_TOKEN"
  else
    echo "❌ Login failed"
  fi
else
  echo "❌ Registration failed - no token generated"
fi

echo ""
echo "✅ Tests completed!"
