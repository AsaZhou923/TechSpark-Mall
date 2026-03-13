# 后端代码优化与重构说明

## 概述

本次优化聚焦于提升 `mall` 后端代码的**正确性、安全性与可维护性**，在不改变现有功能的前提下，修复了若干潜在缺陷并对关键模块进行了针对性改进。

---

## 优化详情

### 1. 修复 `ResultCode.VALIDATE_FAILED` 状态码错误

**文件：** `mall-common/src/main/java/com/xavi/mall/common/api/ResultCode.java`

**问题：** `VALIDATE_FAILED` 原来使用 HTTP 状态码 `404`（Not Found），语义上不正确。参数校验失败应返回 `400`（Bad Request）。

**修改：**
```java
// 修改前
VALIDATE_FAILED(404, "参数检验失败"),

// 修改后
VALIDATE_FAILED(400, "参数检验失败"),
```

**原因：** HTTP 语义规范中，`400 Bad Request` 表示客户端请求参数有误，`404 Not Found` 表示资源不存在。使用错误的状态码会导致前端或第三方调用方对错误类型产生误判。

---

### 2. 增强 `Asserts` 断言工具类

**文件：** `mall-common/src/main/java/com/xavi/mall/common/exception/Asserts.java`

**问题：** 原有 `Asserts` 只提供 `fail()` 方法，缺乏常用的前置条件断言，导致各处代码大量重复编写 `if ... throw` 逻辑。

**新增方法：**
```java
// 断言条件为真，否则抛出 ApiException
public static void isTrue(boolean condition, String message)

// 断言对象不为 null，否则抛出 ApiException
public static void notNull(Object obj, String message)

// 断言字符串不为空，否则抛出 ApiException
public static void notEmpty(String str, String message)
```

**原因：** 统一的断言工具能使 Service 层的参数校验代码更简洁、语义更清晰，并减少重复的 null/空判断逻辑，降低因遗漏校验而引入的 Bug 风险。

---

### 3. 完善全局异常处理器 `GlobalExceptionHandler`

**文件：** `mall-common/src/main/java/com/xavi/mall/common/exception/GlobalExceptionHandler.java`

**问题：** 原有异常处理器仅覆盖了 `ApiException`、`MethodArgumentNotValidException`、`BindException` 和 `SQLSyntaxErrorException`，对于常见的 HTTP 层异常（如请求方法不匹配、缺少参数）缺乏处理，这些异常会以框架默认格式返回，与系统统一的 `CommonResult` 格式不一致。

**新增处理器：**

| 新增处理的异常 | 说明 |
|---|---|
| `MissingServletRequestParameterException` | 缺少必要请求参数时返回友好提示 |
| `HttpRequestMethodNotSupportedException` | 请求方法不匹配时返回友好提示，并附加 `405 Method Not Allowed` 状态码 |

**原因：** 保持 API 响应格式统一，所有异常（包括框架级异常）都以 `CommonResult` 格式返回，便于前端统一处理。

---

### 4. 修复 `JwtTokenUtil.validateToken` 的潜在空指针异常

**文件：** `mall-security/src/main/java/com/xavi/mall/security/util/JwtTokenUtil.java`

**问题：** 当传入非法或格式错误的 Token 时，`getClaimsFromToken()` 会返回 `null`，而原有的 `validateToken()` 方法会直接对返回的 `username`（`null`）调用 `.equals()`，导致 `NullPointerException`。

**修改：**
```java
// 修改前（存在 NPE 风险）
public boolean validateToken(String token, UserDetails userDetails) {
    String username = getUserNameFromToken(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
}

// 修改后（加入 null 检查）
public boolean validateToken(String token, UserDetails userDetails) {
    String username = getUserNameFromToken(token);
    return username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token);
}
```

**原因：** 恶意或损坏的 Token 不应导致系统抛出 `500 Internal Server Error`，而应被安全地拒绝。

---

### 5. 为商品管理 Service 添加事务支持

**文件：** `mall-admin/src/main/java/com/xavi/mall/service/impl/PmsProductServiceImpl.java`

**问题：** `create()` 和 `update()` 方法涉及多张数据表的写操作（商品表、SKU 库存表、会员价格表、阶梯价格表、满减价格表、商品属性值表、专题关联表），但没有加事务注解。一旦某张表的写入失败，已完成的写入无法回滚，导致数据不一致。

**修改：**
```java
@Transactional(rollbackFor = Exception.class)
@Override
public int create(PmsProductParam productParam) { ... }

@Transactional(rollbackFor = Exception.class)
@Override
public int update(Long id, PmsProductParam productParam) { ... }
```

**原因：** 涉及多表写操作的业务方法必须保证原子性，要么全部成功，要么全部回滚，以保证数据库的一致性，避免产生孤立的脏数据。

---

### 6. 修复 SKU 编码生成中的线程安全问题

**文件：** `mall-admin/src/main/java/com/xavi/mall/service/impl/PmsProductServiceImpl.java`

**问题：** `handleSkuStockCode()` 方法中每次调用都会 `new SimpleDateFormat("yyyyMMdd")`。`SimpleDateFormat` 是非线程安全的类，在高并发场景下多个线程同时操作同一实例会导致数据错乱（虽然此处是局部变量，但频繁创建实例也有性能损耗）。更重要的是，推广使用 Java 8 的新日期 API 是更现代的做法。

**修改：**
```java
// 修改前（每次都创建新实例，且使用过时 API）
SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
sb.append(sdf.format(new Date()));

// 修改后（使用线程安全的 DateTimeFormatter 常量）
private static final DateTimeFormatter SKU_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
sb.append(LocalDate.now().format(SKU_DATE_FORMATTER));
```

**原因：** `DateTimeFormatter` 是不可变、线程安全的，推荐在 Java 8+ 项目中替代 `SimpleDateFormat`。将其定义为静态常量可避免重复创建对象，提升性能。

---

### 7. 改进后台用户注册逻辑

**文件：** `mall-admin/src/main/java/com/xavi/mall/service/impl/UmsAdminServiceImpl.java`

**问题 1（错误处理）：** `register()` 在用户名已存在时直接 `return null`，调用方通过判断返回值是否为 `null` 来处理失败情况。这种"以 null 代表错误"的模式会掩盖失败原因，调用方无法区分是"用户名已存在"还是"其他内部错误"。

**问题 2（数据一致性）：** `register()` 方法在插入用户记录后，还会插入角色关联记录，但同样缺少事务保护。

**修改：**
```java
@Transactional(rollbackFor = Exception.class)
@Override
public UmsAdmin register(UmsAdminParam umsAdminParam) {
    // ...
    if (!umsAdminList.isEmpty()) {
        Asserts.fail("该用户名已被使用"); // 抛出异常，由全局异常处理器统一处理
    }
    // ...
}
```

对应地，`UmsAdminController.register()` 移除了对 `null` 返回值的判断，由全局异常处理器统一处理 `ApiException`：

```java
// 修改后
public CommonResult<UmsAdmin> register(@Validated @RequestBody UmsAdminParam umsAdminParam) {
    UmsAdmin umsAdmin = adminService.register(umsAdminParam);
    return CommonResult.success(umsAdmin);
}
```

**原因：** 用异常表达错误状态（相比返回 null）能让错误原因在 API 响应中明确呈现给调用方，并与系统现有的全局异常处理机制保持一致。同时，为 `updateRole()` 也补充了事务注解，保证先删后增的角色关联更新具备原子性。

---

### 8. 使用 `SecureRandom` 生成短信验证码

**文件：** `mall-portal/src/main/java/com/xavi/mall/portal/service/impl/UmsMemberServiceImpl.java`

**问题：** `generateAuthCode()` 使用 `java.util.Random` 生成短信验证码，`Random` 基于线性同余算法，其输出在密码学意义上是可预测的，攻击者可能通过暴力或统计分析推断出验证码。

**修改：**
```java
// 修改前
Random random = new Random();

// 修改后
SecureRandom random = new SecureRandom();
```

**原因：** 短信验证码属于安全凭证，应使用加密安全的伪随机数生成器（CSPRNG）。`java.security.SecureRandom` 使用操作系统提供的高熵源，保证生成结果的不可预测性，是 Java 安全规范的推荐做法。

---

## 优化总结

| # | 文件 | 改动类型 | 核心收益 |
|---|---|---|---|
| 1 | `ResultCode.java` | Bug 修复 | 修正错误的 HTTP 状态码语义 |
| 2 | `Asserts.java` | 功能增强 | 提供更丰富的断言工具，减少冗余代码 |
| 3 | `GlobalExceptionHandler.java` | 功能增强 | 覆盖更多异常类型，统一 API 响应格式 |
| 4 | `JwtTokenUtil.java` | Bug 修复 | 防止非法 Token 引发 NPE |
| 5 | `PmsProductServiceImpl.java` | 正确性改进 | 多表写操作加事务，保证数据一致性 |
| 6 | `PmsProductServiceImpl.java` | 性能/正确性 | 替换 `SimpleDateFormat`，使用线程安全的 `DateTimeFormatter` |
| 7 | `UmsAdminServiceImpl.java` + `UmsAdminController.java` | 设计改进 | 以异常代替 null 返回值，错误信息更明确；补充事务保护 |
| 8 | `UmsMemberServiceImpl.java` | 安全加固 | 使用 `SecureRandom` 生成验证码，防止可预测攻击 |
