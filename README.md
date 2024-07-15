Demo project for online wallet using:

Java 22
Spring Boot
Spring Data JPA
Spring Retry
Gradle
MySQL

How to test:
1. Create the test database tables and import the data (configuration assumes schema is named wallet)
3. Use the gradle wrapper to build and run tests: gradlew build
4. Use the gradle wrapper to deploy: gradlew bootJar

Some curl POST commands:
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
