Demo project for online wallet using:
1. Java 22
2. Spring Boot
3. Spring Data JPA
4. Spring Retry
5. Gradle
6. MySQL

Endpoints provided:
1. GET Get Account by ID: /account/{id}
2. GET Get Account Balance by ID: /account/{id}/balance
3. POST Create Account: /account
4. GET Get all Transactions related to a given Account: /account/{id}/transactions
5. GET Get all outgoing Transactions from a given Account: /account/{id}/transactions/out
6. GET Get all incoming Transactions into a given Account: /account/{id}/transactions/in
7. POST Create Transaction: /transaction

How to test:
1. Create the test database tables and import the data (update application.properties for the configuration as necessary)
3. Use the gradle wrapper to build and run tests: gradlew build
4. Use the gradle wrapper to deploy: gradlew bootJar

Expected data after running tests:
1. account table
  a. id 3 to 6 have updated balance and version values
  b. new record "new_client_<timestamp>" created by account creation
  c. new record "non-existent_destination_<timestamp>" created by transaction with non-existent destination
  d. new record "non-existent_source_<timestamp>" created by transaction with non-existent source 
  e. new records "non-existent_source_<timestamp>" and "non-existent_destination_<timestamp>" created by transaction with non-existent source and destination
  f. new records "lock_test_source" and "lock_test_destination" created by lock test with balance of -100000 and 100000 respectively, and version of 100
2. transaction table
  a. new record from source_id 3 to destination_id 4
  b. new record from source_id 6 to non-existent destination <generated id>
  c. new record from non-existent source <generated id> to destination_id 5
  d. new record from non-existent source <generated id> to non-existent destination <generated id>
  e. 100 new records with source "lock_test_source" and destination "lock_test_destination" with amount of 1000

Some curl POST commands (for cmd; Powershell may need different formatting):
1. Account creation:
curl -H "Content-Type: application/json" -X POST http://localhost:8080/account -d "{ """accountName""": """curl_test""", """balance""": 100000.00 }"
2. Transaction creation for existing accounts (replace account ids accordingly):
curl -H "Content-Type: application/json" -X POST http://localhost:8080/transaction -d "{ """sourceId""": 29, """source""": """curl_test""", """destinationId""": 30, """destination""": """curl_test_2""", """amount""": 50000, """timestamp""": """2023-09-07T09:48:29""" }"
3. Transaction creation for non-existent accounts (fill in account ids for source or destination if needed):
curl -H "Content-Type: application/json" -X POST http://localhost:8080/transaction -d "{ """sourceId""": null, """source""": """curl_test_3""", """destinationId""": null, """destination""": """curl_test_4""", """amount""": 60000, """timestamp""": """2023-09-07T09:48:29""" }"

Out of scope:
1. Validation
2. Exception handling
3. UI/Swagger
