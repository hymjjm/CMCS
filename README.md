
---
# CMCS
Cluster Mutation Classification System

# Project Runtime Environment Description

## Required Software

1. **Java**
   - Required to run the backend code. Recommended version: `JDK 17`.
   - Download link: [Oracle JDK Download](https://www.oracle.com/java/technologies/javase-downloads.html)  
   - Or [OpenJDK Download](https://openjdk.org/)

2. **Maven**
   - Used for managing and building backend dependencies.
   - Download link: [Maven Official Download](https://maven.apache.org/download.cgi)

3. **Node.js**
   - Required to run the frontend code and manage dependencies.
   - Recommended version: `Node.js LTS`.
   - Download link: [Node.js Official Download](https://nodejs.org/)

4. **npm** (automatically installed with Node.js)
   - Used for installing frontend dependencies.
   - Comes bundled with the Node.js installation.

5. **MySQL** (if the backend requires a database)
   - Used for storing project-related data.
   - Recommended version: `MySQL 8.0`.
   - Download link: [MySQL Official Download](https://dev.mysql.com/downloads/)
   - You need to create the required database and user in MySQL, and execute the initialization script provided in the project (`jjm_cluster_upload.sql`) within the created database.

---

## Configuration Modifications (Only modify the listed settings; it is recommended not to alter other configurations)

Open the `src\main\resources\application.yaml` file to configure the database and other necessary settings:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/<database_name>
    username: root
    password: <database_password>
app:
  enable-interceptor: false  # **Set this parameter to false when running locally**

redis:
  enabled: false # **Set this parameter to false when running locally**
```
---

## Project Startup (Command Line Execution)

### Backend Startup
Build the backend service using Maven (execute this only when the project is cloned for the first time or when dependencies are updated):
```bash
mvn clean install
```
Run the backend service:
```bash
mvn spring-boot:run
```

### Frontend Startup
1. Navigate to the frontend directory:
```bash
cd vue
```
2. Install frontend dependencies (execute this only when the project is cloned for the first time or when dependencies are updated):
```bash
npm install 
```
3. Start the frontend development server:
```bash
npm run dev
```

# Notes for Subsequent Startups
For subsequent project startups, you only need to:

1.Run the backend service:
```bash
mvn spring-boot:run
```
2.Navigate to the vue directory and run the frontend service:
```bash
npm run dev
```

