# Webhook Processor

A Spring Boot application that processes webhooks and finds mutual followers and nth-level followers in a social network.

## Features

1. **Mutual Followers**
   - Finds pairs of users who follow each other
   - Example Input:
     ```json
     {
       "users": [
         {"id": 1, "name": "Alice", "follows": [2, 3]},
         {"id": 2, "name": "Bob", "follows": [1]},
         {"id": 3, "name": "Charlie", "follows": [4]},
         {"id": 4, "name": "David", "follows": [3]}
       ]
     }
     ```
   - Example Output:
     ```json
     {
       "regNo": "REG12347",
       "outcome": [[1, 2], [3, 4]]
     }
     ```

2. **Nth Level Followers**
   - Finds followers at the nth level of a given user
   - Example Input:
     ```json
     {
       "users": {
         "n": 2,
         "findId": 1,
         "users": [
           {"id": 1, "name": "Alice", "follows": [2, 3]},
           {"id": 2, "name": "Bob", "follows": [4]},
           {"id": 3, "name": "Charlie", "follows": [4, 5]},
           {"id": 4, "name": "David", "follows": [6]},
           {"id": 5, "name": "Eva", "follows": [6]},
           {"id": 6, "name": "Frank", "follows": []}
         ]
       }
     }
     ```
   - Example Output:
     ```json
     {
       "regNo": "REG12347",
       "outcome": [4, 5]
     }
     ```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Installation

1. Clone the repository
2. Navigate to the project directory
3. Build the project:
   ```bash
   mvn clean install
   ```

### Running the Application

1. Start the application:
   ```bash
   mvn spring-boot:run
   ```

2. The application will be available at `http://localhost:8080`

### API Endpoints

1. Process Webhook:
   ```
   POST /api/webhook
   ```

2. Find Mutual Followers:
   ```
   POST /api/mutual-followers
   ```

3. Find Nth Level Followers:
   ```
   POST /api/nth-followers
   ```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── bajaj/
│   │           ├── controller/
│   │           │   └── WebhookController.java
│   │           ├── service/
│   │           │   ├── WebhookService.java
│   │           │   ├── ShowMutualFollowers.java
│   │           │   └── ShowNthFollowers.java
│   │           ├── model/
│   │           │   ├── WebhookRequest.java
│   │           │   └── WebhookResponse.java
│   │           └── WebhookProcessorApplication.java
│   └── resources/
│       └── application.properties
```

## Testing

Run the tests using:
```bash
mvn test
```

## License

This project is licensed under the MIT License. 