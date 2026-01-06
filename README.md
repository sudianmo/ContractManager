# 合同管理系统 - 快速开始

## 🚀 运行项目

### IDEA

打开 `ContractManagerApplication.java` → 右键 → `Run`

### 命令行

```bash
mvn spring-boot:run
```

### VS Code

安装插件 `Extension Pack for Java` → 点击 `main` 方法上的 `Run`

---

## ⚙️ 数据库配置

### 方式 1：IDEA Database 工具（推荐）

1. 右侧 `Database` 面板 → `+` → `Data Source` → `SQL Server`
2. 填写连接信息：
   - Host: `localhost`
   - Port: `1433`
   - Database: `你的数据库名`
   - User: `你的用户名`
   - Password: `你的密码`
3. 点击 `Test Connection` → 成功后 `Apply`
4. 直接在 IDEA 中执行 SQL、查看表结构

### 方式 2：配置文件

修改 `src/main/resources/application.properties`：

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=你的数据库名
spring.datasource.username=你的用户名
spring.datasource.password=你的密码
```

**注意**：两种方式都支持，IDEA 工具仅用于可视化操作数据库，代码连接仍需配置 `application.properties`

---

## ✅ 测试验证（确认连接成功）

启动项目后，用以下方式测试：

### 1. 浏览器测试

```
http://localhost:8080/api/clients
http://localhost:8080/api/contracts
```

**看到 JSON 返回（哪怕是空数组`[]`）= 连接成功**

### 2. Postman 测试

**查询所有客户**

```
GET http://localhost:8080/api/clients
```

**创建客户**

```
POST http://localhost:8080/api/clients
Content-Type: application/json

{
  "clientName": "测试公司",
  "contactPerson": "张三",
  "phone": "13800138000"
}
```

**查询所有合同**

```
GET http://localhost:8080/api/contracts
```

**创建合同**

```
POST http://localhost:8080/api/contracts
Content-Type: application/json

{
  "contractNo": "CT2024001",
  "contractName": "测试合同",
  "clientId": 1,
  "amount": 100000.00,
  "status": "草稿"
}
```

**更新客户**

```
PUT http://localhost:8080/api/clients/1
Content-Type: application/json

{
  "clientName": "更新后的公司",
  "phone": "13900139000"
}
```

**删除客户**

```
DELETE http://localhost:8080/api/clients/1
```

### 3. 命令行测试

```bash
# 查询
curl http://localhost:8080/api/clients

# 创建
curl -X POST http://localhost:8080/api/clients \
  -H "Content-Type: application/json" \
  -d "{\"clientName\":\"测试公司\",\"phone\":\"13800138000\"}"
```

---

## 👥 4 人分工

### 成员 1 - 输入修改

- **负责接口**：POST、PUT、DELETE（合同、客户的增删改）
- **文件位置**：
  - `controller/ContractController.java` 的 `@PostMapping`, `@PutMapping`, `@DeleteMapping`
  - `controller/ClientController.java` 的 `@PostMapping`, `@PutMapping`, `@DeleteMapping`
  - 对应的 Service 层实现（已完成）

### 成员 2 - 查询

- **负责接口**：GET（列表、单个、条件查询）
- **文件位置**：
  - `controller/ContractController.java` 的 `@GetMapping`
  - `controller/ClientController.java` 的 `@GetMapping`
  - Repository 层自定义查询（已完成）

### 成员 3 - 统计

- **负责接口**：统计报表（金额、数量、月度）
- **需要新建文件**：
  - `controller/StatisticsController.java`
  - `service/StatisticsService.java`
  - 编写统计 SQL

### 成员 4 - 导出日志

- **负责接口**：导出 Excel、查询操作日志
- **需要新建文件**：
  - `controller/ExportController.java`
  - `service/ExportService.java`
  - `controller/LogController.java`

---

## 📝 已完成功能

✅ **Entity 层**（实体类）：`entity/Contract.java`, `entity/Client.java`  
✅ **Repository 层**（数据访问）：`repository/ContractRepository.java`, `repository/ClientRepository.java`  
✅ **Service 层**（业务逻辑）：`service/impl/ContractServiceImpl.java`, `service/impl/ClientServiceImpl.java`  
✅ **Controller 层**（接口）：`controller/ContractController.java`, `controller/ClientController.java`

**已实现的 CRUD 方法**：

- ✅ 创建（POST）
- ✅ 查询所有（GET）
- ✅ 查询单个（GET /{id}）
- ✅ 更新（PUT /{id}）
- ✅ 删除（DELETE /{id}）
- ✅ 分页查询
- ✅ 条件查询

**你们只需要**：

1. 配置数据库连接
2. 运行项目
3. 用上面的测试方法验证
4. 按分工添加新功能

---

## ❗ 常见问题

**端口占用**  
在 `application.properties` 添加：

```properties
server.port=8081
```

**连接失败**  
检查：数据库服务是否启动、用户名密码、数据库名是否存在

**Maven 下载慢**  
编辑 `~/.m2/settings.xml`：

```xml
<mirror>
  <id>aliyun</id>
  <url>https://maven.aliyun.com/repository/public</url>
  <mirrorOf>central</mirrorOf>
</mirror>
```
