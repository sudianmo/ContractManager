# 合同管理系统 - 快速开始

## 安装依赖

克隆项目后执行：

./mvnw clean install

首次执行会自动下载依赖，等待几分钟，看到 BUILD SUCCESS 即可。

---

## 数据库配置关键步骤

### 1. 创建数据库

在 SQL Server 中执行：

CREATE DATABASE ContractManagementSystem;
GO

### 2. 授权用户

方式 1：使用 sa 账户
直接在 application.properties 中配置 sa 账户即可

方式 2：授权现有用户

USE ContractManagementSystem;
CREATE USER ikun FOR LOGIN ikun;
ALTER ROLE db_owner ADD MEMBER ikun;
GO

### 3. 创建表

USE ContractManagementSystem;
GO

CREATE TABLE Client (
id BIGINT IDENTITY(1,1) PRIMARY KEY,
client_name NVARCHAR(100) NOT NULL,
contact_person NVARCHAR(50),
phone NVARCHAR(20),
email NVARCHAR(100),
address NVARCHAR(200),
company_type NVARCHAR(50),
credit_level NVARCHAR(10),
create_time DATE,
update_time DATE
);

CREATE TABLE Contract (
id BIGINT IDENTITY(1,1) PRIMARY KEY,
contract_no NVARCHAR(50) UNIQUE,
contract_name NVARCHAR(100) NOT NULL,
client_id BIGINT,
amount DECIMAL(18,2),
sign_date DATE,
start_date DATE,
end_date DATE,
status NVARCHAR(20),
description NVARCHAR(500),
create_time DATE,
update_time DATE
);

### 4. 配置连接

修改 src/main/resources/application.properties：

spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=ContractManagementSystem;encrypt=true;trustServerCertificate=true
spring.datasource.username=你的用户名
spring.datasource.password=你的密码

---

## 运行项目

IDEA：打开 ContractManagerApplication.java 右键 Run

命令行：./mvnw spring-boot:run

---

## 测试验证

访问以下地址，返回 [] 或 JSON 数据即表示成功：

http://localhost:8080/api/clients
http://localhost:8080/api/contracts

---

## 4 人分工

成员 1- 输入修改
负责 POST PUT DELETE 接口，合同和客户的增删改
文件：controller/ContractController.java, controller/ClientController.java

成员 2 - 查询
负责 GET 接口，列表、单个、条件查询
文件：controller/ContractController.java, controller/ClientController.java

成员 3 - 统计
负责统计报表，金额、数量、月度统计
新建 controller/StatisticsController.java, service/StatisticsService.java

成员 4 - 导出日志
负责导出 Excel、查询操作日志
新建 controller/ExportController.java, service/ExportService.java

---

## 已完成功能

Entity 层：entity/Contract.java, entity/Client.java
Repository 层：repository/ContractRepository.java, repository/ClientRepository.java
Service 层：service/impl/ContractServiceImpl.java, service/impl/ClientServiceImpl.java
Controller 层：controller/ContractController.java, controller/ClientController.java

CRUD 方法：创建 POST, 查询 GET, 更新 PUT, 删除 DELETE, 分页查询, 条件查询

---

## 常见问题

端口占用：在 application.properties 中加 server.port=8081

连接失败：检查数据库服务、用户名密码、数据库名

Maven 下载慢：编辑 ~/.m2/settings.xml 添加阿里云镜像
