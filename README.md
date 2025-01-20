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

## 修改配置文件

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

## 项目启动

###使用 Maven 进行构建并运行后端服务：
mvn clean install
mvn spring-boot:run

###启动前端服务：
1. 进入前端目录
```bash
cd vue
```
2. 安装前端依赖
使用 npm 安装依赖：
```bash
npm install
```
3. 启动前端开发服务器
```bash
npm run dev
```
