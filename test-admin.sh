#!/bin/bash

# Test script for admin account seeding
echo "🧪 Testing Admin Account Seeding..."
echo ""

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Configuration
API_BASE_URL="${API_BASE_URL:-http://localhost:6888}"
ADMIN_USERNAME="${ADMIN_USERNAME:-admin}"
ADMIN_PASSWORD="${ADMIN_PASSWORD:-Admin@123456}"

echo -e "${BLUE}📋 Configuration:${NC}"
echo "   API URL: $API_BASE_URL"
echo "   Username: $ADMIN_USERNAME"
echo "   Password: $ADMIN_PASSWORD"
echo ""

# Test 1: Login with username
echo -e "${YELLOW}Test 1: Login with username${NC}"
response=$(curl -s -X POST "$API_BASE_URL/api/v1/auth/login" \
  -H "Content-Type: application/json" \
  -d "{
    \"usernameOrEmail\": \"$ADMIN_USERNAME\",
    \"password\": \"$ADMIN_PASSWORD\"
  }")

if echo "$response" | jq -e '.success == true' > /dev/null 2>&1; then
  echo -e "${GREEN}✅ Login with username: SUCCESS${NC}"

  # Extract user info
  name=$(echo "$response" | jq -r '.data.user.name')
  email=$(echo "$response" | jq -r '.data.user.email')
  role=$(echo "$response" | jq -r '.data.user.role')
  token=$(echo "$response" | jq -r '.data.token')

  echo "   👤 Name: $name"
  echo "   📧 Email: $email"
  echo "   🔑 Role: $role"
  echo "   🎟️  Token: ${token:0:50}..."
else
  echo -e "${RED}❌ Login with username: FAILED${NC}"
  echo "$response" | jq '.'
fi
echo ""

# Test 2: Login with email
echo -e "${YELLOW}Test 2: Login with email${NC}"
ADMIN_EMAIL="${ADMIN_EMAIL:-admin@musiclearn.studio}"
response=$(curl -s -X POST "$API_BASE_URL/api/v1/auth/login" \
  -H "Content-Type: application/json" \
  -d "{
    \"usernameOrEmail\": \"$ADMIN_EMAIL\",
    \"password\": \"$ADMIN_PASSWORD\"
  }")

if echo "$response" | jq -e '.success == true' > /dev/null 2>&1; then
  echo -e "${GREEN}✅ Login with email: SUCCESS${NC}"

  # Extract user info
  username=$(echo "$response" | jq -r '.data.user.username')
  role=$(echo "$response" | jq -r '.data.user.role')

  echo "   👤 Username: $username"
  echo "   🔑 Role: $role"
else
  echo -e "${RED}❌ Login with email: FAILED${NC}"
  echo "$response" | jq '.'
fi
echo ""

# Test 3: Verify admin role
echo -e "${YELLOW}Test 3: Verify admin has ADMIN role${NC}"
if echo "$response" | jq -e '.data.user.role == "ADMIN"' > /dev/null 2>&1; then
  echo -e "${GREEN}✅ Admin role verification: SUCCESS${NC}"
  echo "   User has ADMIN role"
else
  echo -e "${RED}❌ Admin role verification: FAILED${NC}"
  echo "   User does not have ADMIN role"
fi
echo ""

# Test 4: Try wrong password
echo -e "${YELLOW}Test 4: Login with wrong password (should fail)${NC}"
response=$(curl -s -X POST "$API_BASE_URL/api/v1/auth/login" \
  -H "Content-Type: application/json" \
  -d "{
    \"usernameOrEmail\": \"$ADMIN_USERNAME\",
    \"password\": \"WrongPassword123\"
  }")

if echo "$response" | jq -e '.success == false' > /dev/null 2>&1; then
  echo -e "${GREEN}✅ Wrong password test: SUCCESS (correctly rejected)${NC}"
else
  echo -e "${RED}❌ Wrong password test: FAILED (should have been rejected)${NC}"
fi
echo ""

echo -e "${BLUE}🎉 Testing completed!${NC}"
