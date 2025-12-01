# Bajaj Finserv Health Hiring Challenge - JAVA
🔗 Deploy Link: https://bajaj-finserv-v2c4.onrender.com


## Project Overview
This Spring Boot application automatically:
1. Sends a POST request to generate a webhook on startup
2. Solves the assigned SQL problem based on registration number
3. Submits the SQL solution to the webhook URL using JWT authentication

## Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

## Project Structure
```
bajaj-finserv-hiring-challenge/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── bajajfinserv/
│       │           └── hiring/
│       │               ├── HiringChallengeApplication.java
│       │               ├── config/
│       │               │   └── ApplicationStartupRunner.java
│       │               ├── model/
│       │               │   ├── WebhookRequest.java
│       │               │   ├── WebhookResponse.java
│       │               │   └── SolutionRequest.java
│       │               └── service/
│       │                   ├── WebhookService.java
│       │                   └── SQLSolver.java
│       └── resources/
│           └── application.properties
├── pom.xml
└── README.md
```

## Configuration

Before running the application, update the `application.properties` file with your details:

```properties
# User Configuration (IMPORTANT: Change these values)
user.name=Your Full Name
user.regNo=REG12347
user.email=your.email@example.com
```

## Output Screenshot
<img width="1225" height="722" alt="Screenshot 2025-12-01 at 7 40 54 PM" src="https://github.com/user-attachments/assets/d420a76f-ebf3-4057-a377-2ef67d8729f2" />


## SQL Solutions

### Question 1 (Odd RegNo)
Finds the highest salaried employee per department, excluding payments made on the 1st day of the month.

### Question 2 (Even RegNo)
Calculates average age of employees earning >₹70,000 per department with a concatenated list of up to 10 employee names.

## Building the Project

### Using Maven
```bash
mvn clean package
```

This will generate a JAR file in the `target/` directory named:
`bajaj-finserv-hiring-challenge.jar`

## Running the Application

### From JAR file
```bash
java -jar target/bajaj-finserv-hiring-challenge.jar
```

### From Maven
```bash
mvn spring-boot:run
```

## How It Works

1. **On Startup**: `ApplicationStartupRunner` executes automatically
2. **Generate Webhook**: Sends POST request with user details to generate webhook URL and access token
3. **Determine Question**: Analyzes last digit of regNo (odd → Question 1, even → Question 2)
4. **Solve SQL**: Generates appropriate SQL query
5. **Submit Solution**: Posts the SQL query to webhook URL with JWT token

## Logs
The application provides detailed logging:
- Webhook generation status
- SQL query generated
- Submission status
- Success/failure messages

## API Endpoints Used

### Generate Webhook
- **URL**: `https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA`
- **Method**: POST
- **Body**: 
```json
{
  "name": "Your Name",
  "regNo": "REG12347",
  "email": "your.email@example.com"
}
```

### Submit Solution
- **URL**: Returned webhook URL
- **Method**: POST
- **Headers**: 
  - `Authorization: <accessToken>`
  - `Content-Type: application/json`
- **Body**:
```json
{
  "finalQuery": "YOUR_SQL_QUERY_HERE"
}
```

## Technologies Used
- Spring Boot 3.2.0
- Java 17
- RestTemplate for HTTP requests
- Lombok for reducing boilerplate code
- Maven for build management

## Troubleshooting

### Application fails to start
- Ensure Java 17+ is installed: `java -version`
- Check Maven installation: `mvn -version`

### Webhook generation fails
- Verify internet connectivity
- Check if the API endpoint is accessible
- Ensure user details in `application.properties` are correct

### Solution submission fails
- Check logs for JWT token issues
- Verify the SQL query is valid
- Ensure webhook URL is correct

## Author
Shristi Shrivastava

## Notes
- No controllers are exposed; the entire flow runs on startup
- Application uses `ApplicationRunner` interface for startup execution
- JWT token is automatically included in Authorization header
- SQL queries are optimized for the given schema

## License
This project is created for Bajaj Finserv Health hiring challenge.


