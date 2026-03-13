# TechSpark-Mall 电商系统

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.5-brightgreen)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-1.8-orange)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-blue)](#)

## 项目介绍

**TechSpark-Mall** 是一套完整的全栈电商系统，包含：

- **后端服务（mall）**：基于 Spring Boot + MyBatis 实现的多模块后端，提供后台管理 API 与前台商城 API
- **后台管理前端（mall-admin-web）**：基于 Vue + Element UI 实现的管理后台 SPA
- **移动端（mall-app-web）**：基于 uni-app 实现的移动端商城应用

系统涵盖商品管理、订单管理、会员管理、促销管理、内容管理、搜索等核心电商模块，并集成了 Elasticsearch 搜索、RabbitMQ 消息队列、Redis 缓存等中间件。

---

## 仓库结构

```
TechSpark-Mall/
├── mall/                  # 后端 Spring Boot 多模块项目
│   ├── mall-common/       # 公共工具类、通用响应封装、全局异常处理
│   ├── mall-mbg/          # MyBatis Generator 生成的数据层代码
│   ├── mall-security/     # Spring Security + JWT 封装模块
│   ├── mall-admin/        # 后台管理系统 API 服务
│   ├── mall-portal/       # 前台商城 API 服务
│   ├── mall-search/       # 基于 Elasticsearch 的商品搜索服务
│   └── docs/              # 后端文档（架构说明、重构记录等）
├── mall-admin-web/        # 管理后台前端（Vue + Element UI）
└── mall-app-web/          # 移动端商城（uni-app）
```

---

## 技术栈

### 后端

| 技术 | 说明 | 版本 |
|---|---|---|
| Spring Boot | Web 应用框架 | 2.7.5 |
| Spring Security | 认证与授权框架 | 2.7.5 |
| MyBatis | ORM 框架 | 3.5.10 |
| MyBatis Generator | 数据层代码生成器 | 1.4.1 |
| Elasticsearch | 全文搜索引擎 | 7.17.3 |
| RabbitMQ | 消息队列 | 3.10.5 |
| Redis | 缓存 / 会话存储 | 7.0 |
| MongoDB | 浏览历史等非结构化数据存储 | 5.0 |
| MySQL | 主数据库 | 5.7 |
| Druid | 数据库连接池 | 1.2.14 |
| JWT (jjwt) | 无状态身份认证 | 0.9.1 |
| PageHelper | MyBatis 分页插件 | 5.3.2 |
| Swagger (Springfox) | API 文档生成 | 3.0.0 |
| Hutool | Java 工具类库 | 5.8.9 |
| Lombok | Java 样板代码消除 | 随 Spring Boot |
| MinIO / 阿里云 OSS | 对象存储（图片上传） | — |
| Logstash Logback | 日志采集（ELK 集成） | 7.2 |

### 管理后台前端

| 技术 | 说明 |
|---|---|
| Vue 2 | 核心前端框架 |
| Vue Router | 路由管理 |
| Vuex | 全局状态管理 |
| Element UI | 组件库 |
| Axios | HTTP 请求 |
| vue-element-admin | 项目脚手架参考 |

### 移动端

| 技术 | 说明 |
|---|---|
| uni-app | 跨平台移动端框架 |
| Vue 2 | 核心前端框架 |
| Vuex | 全局状态管理 |
| luch-request | HTTP 请求框架 |

---

## 系统模块说明

### mall-common（公共模块）

- `CommonResult<T>`：统一 API 响应封装（`code` / `message` / `data`）
- `CommonPage<T>`：统一分页响应封装（兼容 PageHelper 和 Spring Data）
- `GlobalExceptionHandler`：全局异常处理（ApiException / 参数校验异常 / HTTP 方法异常等）
- `Asserts`：断言工具类，提供 `fail` / `isTrue` / `notNull` / `notEmpty` 等方法
- `WebLogAspect`：AOP 请求日志切面（URL、参数、响应、耗时、IP）
- `RedisService`：Redis 操作封装接口

### mall-security（安全模块）

- `JwtTokenUtil`：JWT Token 生成、解析、刷新、验证
- `SecurityConfig`：Spring Security 配置（无状态 Session、JWT 过滤器）
- `DynamicSecurityFilter`：基于数据库的动态 URL 权限拦截
- `IgnoreUrlsConfig`：白名单 URL 配置（登录、注册、Swagger 等）

### mall-admin（后台管理 API）

提供商品（PMS）、订单（OMS）、会员（UMS）、营销（SMS）、内容（CMS）五大模块的后台管理接口，共 31 个 Controller。

主要功能：商品 CRUD、SKU 管理、促销规则、订单处理、会员管理、角色权限管理、文件上传。

### mall-portal（前台商城 API）

提供面向消费者的电商接口，包括：商品浏览、购物车、订单生成/支付、会员注册/登录、优惠券、积分等。

集成 RabbitMQ 实现订单超时自动取消，集成支付宝 SDK 实现在线支付。

### mall-search（搜索服务）

基于 Elasticsearch 7.x 提供商品全文搜索，支持：
- 多字段搜索（商品名称、副标题、关键词）
- 分类、品牌、属性聚合筛选
- 相关度 + 销量 + 价格多维度排序

### mall-mbg（代码生成模块）

使用 MyBatis Generator 根据数据库表结构自动生成 Mapper、Model、Example 类，统一管理生成配置。

---

## 快速开始

### 环境依赖

| 工具 | 版本 | 说明 |
|---|---|---|
| JDK | 1.8+ | Java 运行环境 |
| MySQL | 5.7 | 主数据库 |
| Redis | 7.0 | 缓存 |
| MongoDB | 5.0 | 浏览历史等 |
| RabbitMQ | 3.10.5 | 消息队列 |
| Elasticsearch | 7.17.3 | 搜索引擎 |
| Maven | 3.6+ | 构建工具 |
| Node.js | 14+ | 前端构建 |

### 启动中间件

```bash
# 启动 RabbitMQ（并开启管理插件）
rabbitmq-plugins enable rabbitmq_management

# 启动 MongoDB
mongod --dbpath /path/to/mongodb/data

# 启动 MinIO（如使用本地对象存储）
minio server /path/to/minio/data --console-address ":9001"
```

### 后端启动

```bash
cd mall

# 安装依赖并跳过测试
mvn clean install -DskipTests

# 启动后台管理服务（端口 8080）
cd mall-admin
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 启动前台商城服务（端口 8085）
cd ../mall-portal
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 启动搜索服务（端口 8081）
cd ../mall-search
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 管理后台前端启动

```bash
cd mall-admin-web
npm install
npm run dev
```

### 移动端启动

使用 [HBuilderX](https://www.dcloud.io/hbuilderx.html) 打开 `mall-app-web` 目录，选择运行到浏览器或模拟器。

---

## API 文档

各服务启动后，可通过以下地址访问 Swagger UI：

| 服务 | 地址 |
|---|---|
| mall-admin | http://localhost:8080/swagger-ui/ |
| mall-portal | http://localhost:8085/swagger-ui/ |
| mall-search | http://localhost:8081/swagger-ui/ |

---

## 主要功能截图

> 管理后台包含商品管理、订单管理、会员管理、促销管理、权限管理等模块。  
> 移动端包含首页推荐、商品搜索、购物车、订单、会员中心等页面。

---

## 文档

- [后端代码优化与重构说明](mall/docs/REFACTORING.md)：本次优化的详细说明，涵盖 Bug 修复、事务保护、线程安全、安全加固等内容。

---

## 项目结构（后端详细）

```
mall/
├── mall-common/
│   └── src/main/java/com/xavi/mall/common/
│       ├── api/           # CommonResult, CommonPage, ResultCode
│       ├── config/        # Redis, Swagger 基础配置
│       ├── domain/        # WebLog
│       ├── exception/     # ApiException, Asserts, GlobalExceptionHandler
│       ├── log/           # WebLogAspect（AOP 请求日志）
│       ├── service/       # RedisService 接口 & 实现
│       └── util/          # RequestUtil
├── mall-security/
│   └── src/main/java/com/xavi/mall/security/
│       ├── component/     # JWT 过滤器, 动态权限组件
│       ├── config/        # SecurityConfig, IgnoreUrlsConfig
│       └── util/          # JwtTokenUtil, SpringUtil
├── mall-admin/
│   └── src/main/java/com/xavi/mall/
│       ├── controller/    # 31 个 Controller（PMS/OMS/UMS/SMS/CMS）
│       ├── service/       # Service 接口
│       ├── service/impl/  # Service 实现
│       ├── dao/           # 自定义 DAO（含复杂 SQL）
│       ├── dto/           # 请求/响应 DTO
│       └── config/        # MyBatis, Security, OSS, Swagger 配置
├── mall-portal/
│   └── src/main/java/com/xavi/mall/portal/
│       ├── controller/    # 前台 API Controller
│       ├── service/       # 业务 Service
│       ├── domain/        # 领域模型
│       ├── component/     # RabbitMQ 消息处理
│       └── config/        # Security, RabbitMQ, Alipay, Swagger 配置
└── mall-search/
    └── src/main/java/com/xavi/mall/search/
        ├── controller/    # EsProductController
        ├── service/       # 搜索 Service
        ├── repository/    # Spring Data ES Repository
        └── domain/        # ES 文档模型
```
