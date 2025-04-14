## Patient Server using Scala 3 and ZIO 2

> **Note:** Ensure Docker is installed on your system before starting the project. Use the provided `docker-compose.yml` file to set up the database effortlessly.

### Usage

This is a normal sbt project. You can compile code with `sbt compile`, run it with `sbt run` (To run the complete project hit '**1**'), and `sbt console` will start a Scala 3 REPL.

For more information on the sbt-dotty plugin, see the
[scala3-example-project](https://github.com/scala/scala3-example-project/blob/main/README.md).


# Project Name

This project uses environment variables to securely manage configuration values such as database credentials, JWT secrets, email service settings, and file paths. The values in the `.env` file should be configured appropriately for your local development or production environment.

## Getting Started

To get started with the project, follow the instructions below.

### 1. Clone the Repository

Clone the repository to your local machine using Git:

` git clone https://github.com/yash-a-18/patient-server.git `

### 2. Create .env File
Copy the contents from the provided .env.sample file into a new .env file.

Place the .env file in the root directory of your project (next to your src folder).

### 3. Configure Environment Variables
Update the values in the .env file according to your local or production environment. Below is a description of each variable:

**Database Configuration:**

- `DATABASE_JDBC_URL`: The JDBC URL to connect to your PostgreSQL database. Replace localhost:5432/patienttracker with your database host, port, and name.
- `DATABASE_USER`: The username used to connect to the database (e.g., "docker").
- `DATABASE_PASS`: The password associated with the database user (e.g., "docker").

**JWT Configuration:**

- `JWT_SERVICE`: The secret key used to sign JSON Web Tokens (JWT). Ensure it is unique and kept secret.
- `JWT_TTL`: The time-to-live (TTL) for the JWT in milliseconds. The default value 864000 represents 10 days.

**Token Duration:**

- `TOKEN_DURATION`: The expiration duration for tokens in milliseconds. The default value 600000 represents 10 minutes.

**Email Configuration:**

- `EMAIL_HOST`: The SMTP host used for sending emails. The default is "smtp.ethereal.email" (used for testing purposes).
- `EMAIL_PORT`: The port used for the SMTP connection. The default is 587, commonly used for secure email sending.
- `EMAIL_USER`: The username for authentication in your SMTP server.
- `EMAIL_PASS`: The password or authentication token used for the SMTP server.

**File Path:**
- `FILE_PATH`: The file path for storing uploaded files. The default is src/main/resources/uploaded.txt.