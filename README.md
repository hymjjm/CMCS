# CMCS
簇突变检测分类系统
# 项目运行环境说明

## 必须安装的软件

1. **Java**
   - 需要 JDK 运行后端代码，建议安装版本：`JDK 17` 。
   - 下载地址：[Oracle JDK 下载](https://www.oracle.com/java/technologies/javase-downloads.html)
   - 或 [OpenJDK 下载](https://openjdk.org/)

2. **Maven**
   - 用于管理和构建后端依赖。
   - 下载地址：[Maven 官网下载](https://maven.apache.org/download.cgi)

3. **Node.js**
   - 用于运行前端代码并管理依赖。
   - 建议安装版本：`Node.js LTS`。
   - 下载地址：[Node.js 官网下载](https://nodejs.org/)

4. **npm**（随 Node.js 自动安装）
   - 用于安装前端依赖。
   - Node.js 安装完成后会自动附带。

5. **MySQL**（如果后端需要数据库）
   - 用于存储项目所需的数据。
   - 建议安装版本：`MySQL 8.0`。
   - 下载地址：[MySQL 官网下载](https://dev.mysql.com/downloads/)
   - 数据库(MySQL)需要创建对应的数据库和用户，在你创建好的数据库中执行项目里的初始化脚本（ jjm_cluster_upload.sql）


---

## 修改配置文件（这里只列出需要修改的配置，别的配置信息最好不要动）

打开 `src\main\resources\application.yaml` 文件，配置数据库和其他配置信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/<数据库名>
    username: root
    password: <数据库密码>
app:
  enable-interceptor: flase  #    **If running locally, please set this parameter to false**

redis:
  enabled: false # **If running locally, please set this parameter to false**
```
---

## 项目启动（终端命令行运行）

###使用 Maven 进行构建并运行后端服务：
mvn clean install(第一次克隆项目或安装依赖时使用)
mvn spring-boot:run

###启动前端服务：
1. 进入前端目录
```bash
cd vue
```
2. 安装前端依赖(第一次克隆项目或安装依赖时使用)
使用 npm 安装依赖：
```bash
npm install 
```
3. 启动前端开发服务器
```bash
npm run dev
```
*后续再启动项目只需要在终端 mvn spring-boot:run 和进入vue目录运行npm run dev 就能启动




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

##Project Startup (Command Line Execution)

###Backend Startup
Build the backend service using Maven (execute this only when the project is cloned for the first time or when dependencies are updated):
```bash
mvn clean install
```
Run the backend service:
```bash
mvn spring-boot:run
```

###Frontend Startup
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

#Notes for Subsequent Startups
For subsequent project startups, you only need to:
1.Run the backend service:
```bash
mvn spring-boot:run
```
2.Navigate to the vue directory and run the frontend service:
```bash
npm run dev
```

