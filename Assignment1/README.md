# Redis Spring Boot 服务

这是一个最小化的Spring Boot服务，可以连接到Redis数据库。

## 功能特性

- 连接到Redis数据库
- 基本的Redis操作（设置、获取、删除键值对）
- JSON数据存储和搜索
- Redis索引搜索功能
- REST API接口
- 诺贝尔奖数据搜索系统

## 运行要求

- Java 17+
- Maven 3.6+
- Redis服务器

## 快速开始

### 1. 启动Redis服务器

确保Redis服务器在localhost:6379运行。

### 2. 运行应用

```bash
mvn spring-boot:run
```

或者

```bash
mvn clean package
java -jar target/redis-springboot-1.0.0.jar
```

### 3. 测试API

应用启动后，可以通过以下API测试Redis连接：

#### 健康检查
```
GET http://localhost:8080/api/redis/health
```

#### 设置键值对
```
POST http://localhost:8080/api/redis/set?key=test&value=hello
```

#### 获取值
```
GET http://localhost:8080/api/redis/get/test
```

#### 检查键是否存在
```
GET http://localhost:8080/api/redis/exists/test
```

#### 删除键
```
DELETE http://localhost:8080/api/redis/delete/test
```

#### 加载诺贝尔奖数据
```
POST http://localhost:8080/api/redis/load-prize-data
```

#### 按类别和年份范围搜索获奖者数量
```
GET http://localhost:8080/api/redis/search/category-year-range?category=economics&startYear=2013&endYear=2023
```

#### 按关键词搜索获奖者数量
```
GET http://localhost:8080/api/redis/search/motivation-keyword?keyword=poverty
```

#### 按姓名搜索获奖者信息
```
GET http://localhost:8080/api/redis/search/laureate-name?firstName=Paul&lastName=Milgrom
```

## 配置说明

Redis连接配置在 `src/main/resources/application.properties` 中：

- `spring.data.redis.host`: Redis服务器地址（默认：localhost）
- `spring.data.redis.port`: Redis端口（默认：6379）
- `spring.data.redis.password`: Redis密码（默认：空）
- `spring.data.redis.database`: 数据库索引（默认：0）

## 项目结构

```
src/
├── main/
│   ├── java/com/example/
│   │   ├── RedisApplication.java          # 主应用类
│   │   ├── config/
│   │   │   └── RedisConfig.java          # Redis配置
│   │   ├── service/
│   │   │   └── RedisService.java         # Redis服务类
│   │   └── controller/
│   │       └── RedisController.java      # REST控制器
│   └── resources/
│       └── application.properties        # 应用配置
└── pom.xml                              # Maven配置
```

