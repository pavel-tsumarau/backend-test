# Revolut backend test

## Task
Design and implement a RESTful API (including data model and the backing implementation) for
money transfers between accounts.

## Implementation notes:
For this particular test, a simple solution based on ConcurrentHashMap was chosen with simple synchronization in a code block that saves the transaction.
Solution doesn't support any persistence or clustering.

## How to build, test and run
This repository contains maven wrapper scripts, so it's only require correct JDK installed to build and run that service.

**build and run all tests**
```shell script
./mvnw verify
```

**Run service:**
```shell script
 java -jar target/backend-test.jar
```



