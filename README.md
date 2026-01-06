# 合同管理系统 - 数据库课程设计

## 项目简介

本项目是基于 Spring Boot 的合同管理系统后端，配合前端实现完整的前后端分离架构。

### 技术栈

- **后端框架**：Spring Boot 3.2.0
- **数据库**：SQL Server（16 表设计）
- **数据访问**：JdbcTemplate（纯 JDBC，不使用 JPA）
- **API 设计**：RESTful 风格

### 16 表数据库设计

**核心业务表（重点关注）：**

1. `Customers` - 客户表
2. `Contracts` - 合同表
3. `Employees` - 员工表
4. `Projects` - 项目表
5. `SystemLogs` - 系统日志表

**扩展业务表（参考）：** 6. `Products` - 产品表 7. `ContractProducts` - 合同产品关联表 8. `Suppliers` - 供应商表 9. `Payments` - 支付表 10. `Invoices` - 发票表 11. `Approvals` - 审批表 12. `ContractStatistics` - 合同统计表 13. `ContractAttachments` - 合同附件表 14. `SupplierProducts` - 供应商产品关联表 15. `EmployeeRoles` - 员工角色表 16. `ProjectTasks` - 项目任务表

---

## 项目结构说明

### 需要关注的核心代码

**DAO 层（数据访问）- 重点修改区域：**

- `dao/impl/ClientDaoImpl.java` - 客户数据操作（人员 1）
- `dao/impl/ContractDaoImpl.java` - 合同数据操作（人员 1）

**Controller 层（API 接口）- 重点修改区域：**

- `controller/ClientController.java` - 客户接口（人员 1）
- `controller/ContractController.java` - 合同接口（人员 1）
- `controller/ViewQueryController.java` - 视图查询接口（人员 2）
- `controller/TriggerTestController.java` - 触发器测试接口（人员 3）
- `controller/ProcedureController.java` - 存储过程调用接口（人员 4）

### 不需要修改的代码（AI 已完成）

**基础框架：**

- `common/` - 统一返回格式、异常处理
- `entity/` - 实体类（POJO）
- `dto/` - 数据传输对象
- `service/` - 业务逻辑层
- `ContractManagerApplication.java` - 启动类
- `application.properties` - 配置文件

**已实现功能：**

- 客户/合同 CRUD 接口
- 分页查询
- 关键字搜索
- 状态筛选
- 客户筛选
- CORS 跨域配置

---

## 数据库前置任务

**所有人执行以下 SQL 脚本（在已有 16 表基础上）：**

```sql
USE ContractManagementSystem;

-- 1. 添加ContractName列
ALTER TABLE Contracts ADD ContractName NVARCHAR(200);

-- 2. 添加CustomerID列并创建外键
ALTER TABLE Contracts ADD CustomerID INT;
ALTER TABLE Contracts ADD CONSTRAINT FK_Contracts_Customers
FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID);

-- 3. 填充测试数据
UPDATE Contracts
SET ContractName = '合同-' + ContractNumber
WHERE ContractName IS NULL;

UPDATE Contracts SET CustomerID = 1 WHERE ContractID <= 12;
UPDATE Contracts SET CustomerID = 2 WHERE ContractID > 12;

PRINT '数据库修改完成！';
```

---

## 安装依赖

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

---

## 四人任务分工

### 人员 1：数据库设计与基础 CRUD

**任务目标：** 完成 16 表创建、测试数据插入、基础 CRUD 接口实现与验证

**数据库操作（SSMS）：**

- 确认 16 张表已创建
- 插入测试数据（10 条客户 + 10 条员工 + 10 条项目 + 20 条合同）

**Java 代码操作：**

- 文件位置：`dao/impl/ClientDaoImpl.java`、`dao/impl/ContractDaoImpl.java`
- 已实现方法：insert()、update()、deleteById()、selectById()、selectByPage()
- **无需修改**，仅需测试验证

**验证方式：**

```bash
# 测试新增客户
curl -X POST http://localhost:8080/api/clients -H "Content-Type: application/json" -d '{...}'

# 测试查询列表
curl "http://localhost:8080/api/clients?pageNum=1&pageSize=10"

# 测试更新
curl -X PUT http://localhost:8080/api/clients/1 -H "Content-Type: application/json" -d '{...}'

# 测试删除
curl -X DELETE http://localhost:8080/api/clients/1
```

**交付物：**

1. SQL 脚本：`insert_test_data.sql`（30 条测试数据）
2. 测试报告 Word：所有 CRUD 接口测试截图
3. 前端操作演示视频（1-2 分钟）

---

### 人员 2：视图模块

**任务目标：** 在 SSMS 创建 4 个统计视图，Java 封装查询接口

**数据库操作（SSMS）：**

```sql
-- 视图1：合同状态统计
CREATE VIEW V_ContractStatistics AS
SELECT Status, COUNT(*) AS ContractCount, SUM(Amount) AS TotalAmount
FROM Contracts GROUP BY Status;

-- 视图2：客户合同汇总
CREATE VIEW V_CustomerContractSummary AS ...

-- 视图3：即将到期合同预警
CREATE VIEW V_ExpiringContracts AS ...

-- 视图4：员工负责合同统计
CREATE VIEW V_EmployeeContractStats AS ...
```

**Java 代码操作：**

- 文件位置：`dao/impl/ContractDaoImpl.java`（添加方法）
- 文件位置：`controller/ContractController.java`（添加接口）
- **需补充**：4 个视图查询方法和 4 个 API 接口

**代码示例（需补充到 ContractDaoImpl）：**

```java
public List<Map<String, Object>> getContractStatistics() {
    return jdbcTemplate.queryForList("SELECT * FROM V_ContractStatistics");
}
```

**验证方式：**

```bash
# SSMS验证视图
SELECT * FROM V_ContractStatistics;

# API验证
curl "http://localhost:8080/api/contracts/statistics/status"
```

**交付物：**

1. SQL 脚本：`create_views.sql`（4 个视图）
2. 测试报告 Word：SSMS 和 API 测试截图对比
3. Postman Collection 导出文件

---

### 人员 3：触发器模块

**任务目标：** 创建 3 个业务触发器，Java 验证自动执行效果

**数据库操作（SSMS）：**

```sql
-- 触发器1：合同金额变更审计日志
CREATE TRIGGER TR_Contracts_AmountChange ON Contracts AFTER UPDATE AS ...

-- 触发器2：客户状态变更记录
CREATE TRIGGER TR_Customers_StatusChange ON Customers AFTER UPDATE AS ...

-- 触发器3：合同总额自动计算
CREATE TRIGGER TR_ContractProducts_UpdateTotal ON ContractProducts AFTER INSERT, UPDATE, DELETE AS ...
```

**Java 代码操作：**

- 文件位置：`controller/TriggerTestController.java`
- **需补充**：测试触发器的接口方法

**代码示例（需补充到 TriggerTestController）：**

```java
@PostMapping("/test-contract-amount")
public Result<Map<String, Object>> testContractAmountTrigger(
    @RequestParam Long contractId, @RequestParam BigDecimal newAmount) {
    // 1. 查询修改前金额
    // 2. 更新金额（触发器自动记录日志）
    // 3. 查询SystemLogs验证触发器执行
}
```

**验证方式：**

```bash
# 触发触发器
curl -X POST "http://localhost:8080/api/trigger-test/test-contract-amount?contractId=3&newAmount=2000000"

# 验证日志
SELECT TOP 5 * FROM SystemLogs WHERE TargetID = 3;
```

**交付物：**

1. SQL 脚本：`create_triggers.sql`（3 个触发器）
2. 测试报告 Word：触发前/触发后/日志记录对比截图
3. SystemLogs 导出 Excel

---

### 人员 4：存储过程模块

**任务目标：** 创建 3 个业务存储过程，Java 调用执行并验证

**数据库操作（SSMS）：**

```sql
-- 存储过程1：批量更新合同状态
CREATE PROCEDURE SP_BatchUpdateContractStatus
    @OldStatus NVARCHAR(20), @NewStatus NVARCHAR(20), @UpdatedCount INT OUTPUT
AS BEGIN ... END;

-- 存储过程2：客户合同统计报表
CREATE PROCEDURE SP_GetCustomerContractReport @CustomerID INT AS ...

-- 存储过程3：月度合同统计
CREATE PROCEDURE SP_MonthlyContractStatistics @Year INT, @Month INT AS ...
```

**Java 代码操作：**

- 文件位置：`controller/ProcedureController.java`
- **需补充**：调用存储过程的接口方法

**代码示例（需补充到 ProcedureController）：**

```java
@PostMapping("/batch-update-status")
public Result<Map<String, Object>> batchUpdateStatus(
    @RequestParam String oldStatus, @RequestParam String newStatus) {
    SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
        .withProcedureName("SP_BatchUpdateContractStatus");
    // 执行存储过程
}
```

**验证方式：**

```bash
# SSMS直接测试
DECLARE @Count INT;
EXEC SP_BatchUpdateContractStatus 'Executing', 'Signed', @Count OUTPUT;

# API测试
curl -X POST "http://localhost:8080/api/procedures/batch-update-status?oldStatus=Executing&newStatus=Signed"
```

**交付物：**

1. SQL 脚本：`create_procedures.sql`（3 个存储过程）
2. 测试报告 Word：SSMS 直接测试和 API 测试对比截图
3. 性能对比报告（可选）

---

---

## AI 已完成功能汇总

**基础 CRUD（95%完成）：**

- Entity 层：entity/Contract.java、entity/Client.java
- DAO 层：dao/impl/ContractDaoImpl.java、dao/impl/ClientDaoImpl.java
- Service 层：service/impl/ContractServiceImpl.java、service/impl/ClientServiceImpl.java
- Controller 层：controller/ContractController.java、controller/ClientController.java

**高级功能框架（40%完成）：**

- 视图查询：ViewQueryController.java（框架已创建，需补充代码）
- 触发器测试：TriggerTestController.java（框架已创建，需补充代码）
- 存储过程调用：ProcedureController.java（框架已创建，需补充代码）

**前端集成（90%完成）：**

- 分页查询、关键字搜索、状态筛选、客户筛选
- 状态映射中文化、渐变背景动画
- CORS 跨域配置

---

## 常见问题

端口占用：在 application.properties 中加 server.port=8081

连接失败：检查数据库服务、用户名密码、数据库名

Maven 下载慢：编辑 ~/.m2/settings.xml 添加阿里云镜像
