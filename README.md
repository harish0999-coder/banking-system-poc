## 📁 DOCUMENTATION FILES

### 1. `README.md` (Root Directory)
````markdown
# Banking System POC - Two-Tier Transaction Processing

A Proof of Concept (POC) demonstrating a simplified banking system with two-tier transaction processing, card range-based routing, and secure PIN authentication.

## 🏗️ Architecture
````
┌─────────────┐      ┌─────────────┐      ┌─────────────┐
│   React UI  │ ───► │  System 1   │ ───► │  System 2   │
│  (Port 5173)│      │   Gateway   │      │ Core Banking│
│             │ ◄─── │ (Port 8081) │ ◄─── │ (Port 8082) │
└─────────────┘      └─────────────┘      └─────────────┘
System Components

System 1 (Gateway): Validates requests and routes transactions based on card range
System 2 (Core Banking): Processes transactions, validates PINs, manages balances
React UI: Customer and Admin dashboards

✨ Features

✅ Two-tier transaction processing
✅ Card range-based routing (only cards starting with '4')
✅ SHA-256 PIN hashing (secure authentication)
✅ In-memory H2 database
✅ Role-based access (Customer & Super Admin)
✅ Transaction history tracking
✅ Real-time balance updates
✅ RESTful APIs

🛠️ Technology Stack
ComponentTechnologyBackendJava 17 + Spring Boot 3.2.0DatabaseH2 (In-Memory)FrontendReact 18 + TypeScriptSecuritySHA-256 HashingBuild ToolsMaven, npm/Vite
📋 Prerequisites

JDK 17 or higher
Node.js 18+ and npm
Maven 3.8+
VS Code or IntelliJ IDEA

🚀 Quick Start
Step 1: Clone Repository
bashgit clone <your-repo-url>
cd banking-system-poc
Step 2: Start System 2 (Core Banking)
bashcd system2-corebank
mvn clean install
mvn spring-boot:run
System 2 will start on http://localhost:8082
Step 3: Start System 1 (Gateway)
Open a new terminal:
bashcd system1-gateway
mvn clean install
mvn spring-boot:run
System 1 will start on http://localhost:8081
Step 4: Start React UI
Open a new terminal:
bashcd banking-ui
npm install
npm run dev
UI will start on http://localhost:5173
🔐 Test Credentials
Customer Login

Username: cust1
Password: pass
Card Number: 4123456789012345
PIN: 1234
Initial Balance: $1000.00

Super Admin Login

Username: admin
Password: admin

Additional Test Cards
Card NumberPINBalanceCustomer Name41234567890123451234$1000.00John Doe49876543210987655678$2500.00Jane Smith51112222333344449999$500.00Test User (Rejected)
Note: Card starting with '5' will be rejected by System 1 (card range not supported)
📡 API Endpoints
System 1 - Gateway (Port 8081)
Process Transaction
httpPOST /api/transaction
Content-Type: application/json

{
  "cardNumber": "4123456789012345",
  "pin": "1234",
  "amount": 50.00,
  "type": "withdraw"
}
System 2 - Core Banking (Port 8082)
Process Transaction
httpPOST /api/process
Content-Type: application/json

{
  "cardNumber": "4123456789012345",
  "pin": "1234",
  "amount": 100.00,
  "type": "topup"
}
Get Balance
httpGET /api/customer/balance/{cardNumber}
Get Customer Transactions
httpGET /api/customer/transactions/{cardNumber}
Get All Transactions (Admin)
httpGET /api/customer/transactions/all
🧪 Testing with cURL
Successful Withdrawal
bashcurl -X POST http://localhost:8081/api/transaction \
  -H "Content-Type: application/json" \
  -d '{
    "cardNumber": "4123456789012345",
    "pin": "1234",
    "amount": 50,
    "type": "withdraw"
  }'
Successful Top-up
bashcurl -X POST http://localhost:8081/api/transaction \
  -H "Content-Type: application/json" \
  -d '{
    "cardNumber": "4123456789012345",
    "pin": "1234",
    "amount": 100,
    "type": "topup"
  }'
Invalid PIN (Should Fail)
bashcurl -X POST http://localhost:8081/api/transaction \
  -H "Content-Type: application/json" \
  -d '{
    "cardNumber": "4123456789012345",
    "pin": "9999",
    "amount": 50,
    "type": "withdraw"
  }'
Unsupported Card Range (Should Fail)
bashcurl -X POST http://localhost:8081/api/transaction \
  -H "Content-Type: application/json" \
  -d '{
    "cardNumber": "5111222233334444",
    "pin": "9999",
    "amount": 50,
    "type": "withdraw"
  }'
Insufficient Balance (Should Fail)
bashcurl -X POST http://localhost:8081/api/transaction \
  -H "Content-Type: application/json" \
  -d '{
    "cardNumber": "4123456789012345",
    "pin": "1234",
    "amount": 10000,
    "type": "withdraw"
  }'
````

## 📊 Database Access

H2 Console is available at: **http://localhost:8082/h2-console**

- **JDBC URL**: `jdbc:h2:mem:bankdb`
- **Username**: `sa`
- **Password**: (leave empty)

## 🔒 Security Features

1. **PIN Hashing**: All PINs are hashed using SHA-256 before storage
2. **Never Log PINs**: Plain-text PINs are never logged or stored
3. **Card Masking**: Card numbers are masked in logs (****1234)
4. **Validation**: Multiple layers of validation before processing

## 📁 Project Structure
````
banking-system-poc/
├── system1-gateway/
│   ├── src/main/java/com/bank/poc/gateway/
│   │   ├── controller/
│   │   │   └── TransactionController.java
│   │   ├── dto/
│   │   │   ├── TransactionRequest.java
│   │   │   └── TransactionResponse.java
│   │   ├── config/
│   │   │   ├── SecurityConfig.java
│   │   │   └── RestTemplateConfig.java
│   │   └── GatewayApplication.java
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
│
├── system2-corebank/
│   ├── src/main/java/com/bank/poc/core/
│   │   ├── entity/
│   │   │   ├── Card.java
│   │   │   └── Transaction.java
│   │   ├── repository/
│   │   │   ├── CardRepository.java
│   │   │   └── TransactionRepository.java
│   │   ├── service/
│   │   │   ├── CardService.java
│   │   │   └── TransactionService.java
│   │   ├── controller/
│   │   │   ├── ProcessingController.java
│   │   │   └── CustomerController.java
│   │   ├── dto/
│   │   │   ├── ProcessRequest.java
│   │   │   └── ProcessResponse.java
│   │   ├── config/
│   │   │   ├── SecurityConfig.java
│   │   │   └── DataInitializer.java
│   │   └── CoreBankApplication.java
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
│
├── banking-ui/
│   ├── src/
│   │   ├── components/
│   │   │   ├── Login.tsx
│   │   │   └── Navbar.tsx
│   │   ├── pages/
│   │   │   ├── CustomerDashboard.tsx
│   │   │   └── AdminDashboard.tsx
│   │   ├── services/
│   │   │   └── api.ts
│   │   ├── types/
│   │   │   └── types.ts
│   │   ├── App.tsx
│   │   ├── main.tsx
│   │   └── index.css
│   ├── package.json
│   ├── vite.config.ts
│   ├── tsconfig.json
│   └── index.html
│
└── README.md
✅ Test Cases
1. Successful Withdrawal

Card: 4123456789012345, PIN: 1234, Amount: $50
Expected: Success, balance reduced to $950

2. Successful Top-up

Card: 4123456789012345, PIN: 1234, Amount: $200
Expected: Success, balance increased to $1200

3. Invalid Card Number

Card: 9999999999999999, PIN: 1234, Amount: $50
Expected: Failed - "Invalid card"

4. Invalid PIN

Card: 4123456789012345, PIN: 9999, Amount: $50
Expected: Failed - "Invalid PIN"

5. Insufficient Balance

Card: 4123456789012345, PIN: 1234, Amount: $10000
Expected: Failed - "Insufficient balance"

6. Unsupported Card Range

Card: 5111222233334444, PIN: 9999, Amount: $50
Expected: Failed - "Card range not supported"

7. Super Admin Dashboard

Login as admin
Expected: View all transactions from all customers

8. Customer Dashboard

Login as cust1
Expected: View own balance, transactions, perform top-ups

🎯 Success Criteria

 System 1 routes transactions based on card range (only '4' accepted)
 System 2 validates card, PIN, and balance
 PIN hashing using SHA-256
 Super Admin sees all transactions
 Customer sees own data and can top-up
 All test cases pass
 No plain-text PINs stored or logged

🐛 Troubleshooting
Port Already in Use
bash# Kill process on port 8081
lsof -ti:8081 | xargs kill -9

# Kill process on port 8082
lsof -ti:8082 | xargs kill -9

# Kill process on port 5173
lsof -ti:5173 | xargs kill -9
Maven Build Issues
bash# Clean and rebuild
mvn clean install -U
React Build Issues
bash# Clear cache and reinstall
rm -rf node_modules package-lock.json
npm install
````

## 📝 Notes

- This is a POC for educational purposes
- Uses in-memory database (data is lost on restart)
- Hardcoded user credentials (not for production)
- No JWT/OAuth implementation (simplified authentication)

## 👨‍💻 Author

**Your Name**
- GitHub: [@yourusername](https://github.com/yourusername)
- Email: your.email@example.com

## 📄 License

This project is licensed under the MIT License.

## 🙏 Acknowledgments

- PayTabs Global for the assignment specification
- Spring Boot Documentation
- React Documentation
````

### 2. `SETUP.md` (Detailed Setup Guide)
````markdown
# Detailed Setup Guide - Banking System POC

## Table of Contents
1. [System Requirements](#system-requirements)
2. [Installation Steps](#installation-steps)
3. [Project Creation from Scratch](#project-creation-from-scratch)
4. [Configuration](#configuration)
5. [Running the Application](#running-the-application)
6. [Verification](#verification)

## System Requirements

### Required Software

| Software | Version | Download Link |
|----------|---------|---------------|
| Java JDK | 17 or higher | https://adoptium.net/ |
| Maven | 3.8+ | https://maven.apache.org/download.cgi |
| Node.js | 18+ | https://nodejs.org/ |
| npm | 9+ | Comes with Node.js |
| VS Code | Latest | https://code.visualstudio.com/ |

### Recommended VS Code Extensions

- Extension Pack for Java (Microsoft)
- Spring Boot Extension Pack
- ES7+ React/Redux/React-Native snippets
- Prettier - Code formatter
- REST Client

## Installation Steps

### 1. Install Java JDK 17

#### Windows:
```bash
# Download from https://adoptium.net/
# Install and set JAVA_HOME environment variable
# Add %JAVA_HOME%\bin to PATH
```

#### macOS:
```bash
brew install openjdk@17
```

#### Linux:
```bash
sudo apt update
sudo apt install openjdk-17-jdk
```

Verify installation:
```bash
java -version
# Should show: openjdk version "17.x.x"
```

### 2. Install Maven

#### Windows:
```bash
# Download from https://maven.apache.org/download.cgi
# Extract and add bin folder to PATH
```

#### macOS:
```bash
brew install maven
```

#### Linux:
```bash
sudo apt install maven
```

Verify installation:
```bash
mvn -version
# Should show: Apache Maven 3.x.x
```

### 3. Install Node.js and npm

#### All Platforms:
Download from https://nodejs.org/ (LTS version recommended)

Verify installation:
```bash
node --version
# Should show: v18.x.x or higher

npm --version
# Should show: 9.x.x or higher
```

## Project Creation from Scratch

### Step 1: Create Project Directory
```bash
mkdir banking-system-poc
cd banking-system-poc
```

### Step 2: Create System 1 (Gateway)

#### Option A: Using Spring Initializr (Recommended)

1. Go to https://start.spring.io/
2. Configure:
   - Project: Maven
   - Language: Java
   - Spring Boot: 3.2.0
   - Group: com.bank.poc
   - Artifact: system1-gateway
   - Package name: com.bank.poc.gateway
   - Packaging: Jar
   - Java: 17

3. Add Dependencies:
   - Spring Web
   - Spring Security
   - Lombok

4. Click "Generate" and extract to `banking-system-poc/system1-gateway`

#### Option B: Manual Creation
```bash
cd banking-system-poc
mkdir -p system1-gateway/src/main/java/com/bank/poc/gateway
mkdir -p system1-gateway/src/main/resources
mkdir -p system1-gateway/src/test/java
```

Copy the `pom.xml` provided in the code files.

### Step 3: Create System 2 (Core Banking)

Same process as System 1, but:
- Artifact: system2-corebank
- Package: com.bank.poc.core
- Additional Dependencies:
  - Spring Data JPA
  - H2 Database
  - Commons Codec

### Step 4: Create React UI
```bash
cd banking-system-poc
npm create vite@latest banking-ui -- --template react-ts
cd banking-ui
npm install axios react-router-dom
```

## Configuration

### System 1 Configuration

File: `system1-gateway/src/main/resources/application.properties`
```properties
server.port=8081
spring.application.name=system1-gateway
system2.url=http://localhost:8082
logging.level.com.bank.poc=INFO
```

### System 2 Configuration

File: `system2-corebank/src/main/resources/application.properties`
```properties
server.port=8082
spring.application.name=system2-corebank

# H2 Database
spring.datasource.url=jdbc:h2:mem:bankdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

logging.level.com.bank.poc=INFO
```

### React UI Configuration

File: `banking-ui/vite.config.ts`
```typescript
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173
  }
})
```

## Running the Application

### Terminal 1: Start System 2 (Must start first!)
```bash
cd banking-system-poc/system2-corebank
mvn clean install
mvn spring-boot:run
```

Wait for message: "System 2 Core Banking Started on Port 8082"

### Terminal 2: Start System 1
```bash
cd banking-system-poc/system1-gateway
mvn clean install
mvn spring-boot:run
```

Wait for message: "System 1 Gateway Started on Port 8081"

### Terminal 3: Start React UI
```bash
cd banking-system-poc/banking-ui
npm install
npm run dev
```

Wait for message: "Local: http://localhost:5173/"

## Verification

### 1. Check System 2 Health
```bash
curl http://localhost:8082/api/health
# Expected: "System 2 Core Banking is running"
```

### 2. Check System 1 Health
```bash
curl http://localhost:8081/api/health
# Expected: "System 1 Gateway is running"
```

### 3. Test Transaction Flow
```bash
curl -X POST http://localhost:8081/api/transaction \
  -H "Content-Type: application/json" \
  -d '{
    "cardNumber": "4123456789012345",
    "pin": "1234",
    "amount": 10,
    "type": "topup"
  }'
```

Expected response:
```json
{
  "success": true,
  "message": "Top-up successful",
  "data": {
    "transactionId": 1,
    "newBalance": 1010.0
  },
  "timestamp": "2024-..."
}
```

### 4. Access UI

Open browser: http://localhost:5173

Login with:
- Customer: `cust1` / `pass`
- Admin: `admin` / `admin`

### 5. Access H2 Console

Open browser: http://localhost:8082/h2-console

- JDBC URL: `jdbc:h2:mem:bankdb`
- Username: `sa`
- Password: (empty)

Run query:
```sql
SELECT * FROM CARDS;
SELECT * FROM TRANSACTIONS;
```

## Common Issues and Solutions

### Issue: Port already in use

**Solution:**
```bash
# Windows
netstat -ano | findstr :8081
taskkill /PID <PID> /F

# macOS/Linux
lsof -ti:8081 | xargs kill -9
lsof -ti:8082 | xargs kill -9
lsof -ti:5173 | xargs kill -9
```

### Issue: Maven dependencies not downloading

**Solution:**
```bash
mvn clean install -U
# or delete .m2/repository and retry
```

### Issue: React modules not found

**Solution:**
```bash
rm -rf node_modules package-lock.json
npm install
```

### Issue: CORS errors in browser

**Solution:**
Check that both Spring Boot applications have:
```java
@CrossOrigin(origins = "*")
```
on controllers.

### Issue: System 1 cannot connect to System 2

**Solution:**
1. Ensure System 2 started first
2. Check `application.properties` in System 1:
```properties
   system2.url=http://localhost:8082
```
3. Verify System 2 is running: `curl http://localhost:8082/api/health`

## Next Steps

1. ✅ All systems running
2. ✅ Test with cURL
3. ✅ Test UI login
4. ✅ Perform transactions
5. ✅ Check H2 database
6. ✅ Review logs
7. ✅ Test all scenarios

## Development Tips

### Hot Reload

- **Spring Boot**: Use Spring Boot DevTools
- **React**: Vite provides automatic hot reload

### Debugging

#### Spring Boot in VS Code:
1. Open Java file
2. Set breakpoint (F9)
3. Press F5 to debug

#### React in VS Code:
1. Install "Debugger for Chrome" extension
2. Add launch configuration
3. Press F5

### Logging

View logs in terminal where applications are running.

Increase log level in `application.properties`:
```properties
logging.level.com.bank.poc=DEBUG
```

## Production Considerations

This POC is for educational purposes. For production:

1. Replace in-memory H2 with persistent database (PostgreSQL/MySQL)
2. Implement proper authentication (JWT/OAuth2)
3. Add input validation with Bean Validation
4. Implement exception handling globally
5. Add API rate limiting
6. Use environment-specific configurations
7. Add comprehensive logging and monitoring
8. Implement database migrations (Flyway/Liquibase)
9. Add unit and integration tests
10. Containerize with Docker

## Support

For issues or questions:
1. Check logs in terminal
2. Verify all systems are running
3. Test with cURL before UI
4. Check H2 console for data
5. Review this guide

## References

- Spring Boot Docs: https://spring.io/projects/spring-boot
- React Docs: https://react.dev/
- H2 Database: https://www.h2database.com/
- Maven: https://maven.apache.org/
- Vite: https://vitejs.dev/
````

### 3. `API_DOCUMENTATION.md`
````markdown
# API Documentation - Banking System POC

## Base URLs

- **System 1 (Gateway)**: `http://localhost:8081/api`
- **System 2 (Core Banking)**: `http://localhost:8082/api`

## System 1 - Gateway APIs

### 1. Process Transaction

Routes transaction to System 2 after validation.

**Endpoint:** `POST /api/transaction`

**Headers:**
````
Content-Type: application/json
Request Body:
json{
  "cardNumber": "string (required)",
  "pin": "string (required)",
  "amount": "number (required, > 0)",
  "type": "string (required, 'withdraw' or 'topup')"
}
Example Request:
json{
  "cardNumber": "4123456789012345",
  "pin": "1234",
  "amount": 100.00,
  "type": "withdraw"
}
Success Response (200 OK):
json{
  "success": true,
  "message": "Withdrawal successful",
  "data": {
    "transactionId": 1,
    "newBalance": 900.00
  },
  "timestamp": "2024-01-15T10:30:00"
}
Error Responses:
400 Bad Request - Validation Error:
json{
  "success": false,
  "message": "Card number is required",
  "data": null,
  "timestamp": "2024-01-15T10:30:00"
}
403 Forbidden - Card Range Not Supported:
json{
  "success": false,
  "message": "Card range not supported",
  "data": null,
  "timestamp": "2024-01-15T10:30:00"
}
````

**Validation Rules:**
- Card number must not be empty
- PIN must not be empty
- Amount must be greater than 0
- Type must be either "withdraw" or "topup"
- Card number must start with '4'

### 2. Health Check

**Endpoint:** `GET /api/health`

**Response (200 OK):**
````
System 1 Gateway is running
````

---

## System 2 - Core Banking APIs

### 1. Process Transaction

Validates card, PIN, and processes the transaction.

**Endpoint:** `POST /api/process`

**Headers:**
````
Content-Type: application/json
Request Body:
json{
  "cardNumber": "string (required)",
  "pin": "string (required)",
  "amount": "number (required, > 0)",
  "type": "string (required, 'withdraw' or 'topup')"
}
Success Response (200 OK):
json{
  "success": true,
  "message": "Withdrawal successful",
  "data": {
    "transactionId": 1,
    "newBalance": 900.00
  },
  "timestamp": "2024-01-15T10:30:00"
}
Error Responses:
Invalid Card:
json{
  "success": false,
  "message": "Invalid card",
  "data": null,
  "timestamp": "2024-01-15T10:30:00"
}
Invalid PIN:
json{
  "success": false,
  "message": "Invalid PIN",
  "data": null,
  "timestamp": "2024-01-15T10:30:00"
}
Insufficient Balance:
json{
  "success": false,
  "message": "Insufficient balance",
  "data": null,
  "timestamp": "2024-01-15T10:30:00"
}
2. Get Balance
Retrieves balance for a specific card.
Endpoint: GET /api/customer/balance/{cardNumber}
Path Parameters:

cardNumber (string, required): Card number

Example: GET /api/customer/balance/4123456789012345
Success Response (200 OK):
json{
  "success": true,
  "message": "Balance retrieved",
  "data": {
    "cardNumber": "****2345",
    "balance": 1000.00,
    "customerName": "John Doe"
  },
  "timestamp": "2024-01-15T10:30:00"
}
3. Get Customer Transactions
Retrieves transaction history for a specific card.
Endpoint: GET /api/customer/transactions/{cardNumber}
Path Parameters:

cardNumber (string, required): Card number

Example: GET /api/customer/transactions/4123456789012345
Success Response (200 OK):
json{
  "success": true,
  "message": "Transactions retrieved",
  "data": [
    {
      "id": 1,
      "cardNumber": "4123456789012345",
      "type": "withdraw",
      "amount": 50.00,
      "timestamp": "2024-01-15T10:30:00",
      "status": "SUCCESS",
      "reason": "Withdrawal successful"
    },
    {
      "id": 2,
      "cardNumber": "4123456789012345",
      "type": "topup",
      "amount": 200.00,
      "timestamp": "2024-01-15T11:00:00",
      "status": "SUCCESS",
      "reason": "Top-up successful"
    }
  ],
  "timestamp": "2024-01-15T12:00:00"
}
4. Get All Transactions (Admin)
Retrieves all transactions across all cards.
Endpoint: GET /api/customer/transactions/all
Success Response (200 OK):
json{
  "success": true,
  "message": "All transactions retrieved",
  "data": [
    {
      "id": 1,
      "cardNumber": "4123456789012345",
      "type": "withdraw",
      "amount": 50.00,
      "timestamp": "2024-01-15T10:30:00",
      "status": "SUCCESS",
      "reason": "Withdrawal successful"
    },
    {
      "id": 2,
      "cardNumber": "4987654321098765",
      "type": "topup",
      "amount": 100.00,
      "timestamp": "2024-01-15T11:00:00",
      "status": "SUCCESS",
      "reason": "Top-up successful"
    }
  ],
  "timestamp": "2024-01-15T12:00:00"
}
````

### 5. Health Check

**Endpoint:** `GET /api/health`

**Response (200 OK):**
````
System 2 Core Banking is running

Complete cURL Examples
Successful Withdrawal
bashcurl -X POST http://localhost:8081/api/transaction \
  -H "Content-Type: application/json" \
  -d '{
    "cardNumber": "4123456789012345",
    "pin": "1234",
    "amount": 50,
    "type": "withdraw"
  }'
Successful Top-up
bashcurl -X POST http://localhost:8081/api/transaction \
  -H "Content-Type: application/json" \
  -d '{
    "cardNumber": "4123456789012345",
    "pin": "1234",
    "amount": 100,
    "type": "topup"
  }'
Get Balance
bashcurl http://localhost:8082/api/customer/balance/4123456789012345
Get Customer Transactions
bashcurl http://localhost:8082/api/customer/transactions/4123456789012345
Get All Transactions (Admin)
bashcurl http://localhost:8082/api/customer/transactions/all
Test Invalid PIN
bashcurl -X POST http://localhost:8081/api/transaction \
  -H "Content-Type: application/json" \
  -d '{
    "cardNumber": "4123456789012345",
    "pin": "9999",
    "amount": 50,
    "type": "withdraw"
  }'
Test Unsupported Card Range
bashcurl -X POST http://localhost:8081/api/transaction \
  -H "Content-Type: application/json" \
  -d '{
    "cardNumber": "5111222233334444",
    "pin


run application:
    🚀 NOW START BOTH SYSTEMS
Terminal 1: Start System 2
powershell$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
cd D:\banking\system2-corebank
.\mvnw.cmd spring-boot:run
WAIT until you see "Started CoreBankApplication" - Keep this terminal running!

Terminal 2: Start System 1 (Open NEW PowerShell Window)
powershell$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
cd D:\banking\system1-gateway
.\mvnw.cmd spring-boot:run
WAIT until you see "Started GatewayApplication" - Keep this terminal running!

Terminal 3: React UI (Already Running)
Keep your React terminal running!

✅ Test Everything
Once both servers are running, go to: http://localhost:5173
Login with: cust1 / pass
