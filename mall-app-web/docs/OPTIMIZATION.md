# mall-app-web 前端代码优化说明

## 概述

本次优化聚焦于 `mall-app-web`（uni-app 移动端商城）的**安全性、代码规范和健壮性**三个维度，在不改变现有功能的前提下，清除调试代码、修复安全漏洞、改进错误处理，并修正函数命名拼写错误。

---

## 优化详情

### 1. 清除全部调试用 `console` 语句

**涉及文件（31 处）：**

| 文件 | 原问题 |
|---|---|
| `App.vue` | `onShow` / `onHide` 中残留 `console.log` |
| `store/index.js` | `login` mutation 中打印 `state.userInfo` |
| `utils/requestUtil.js` | 响应错误拦截器中 `console.log('response error', ...)` |
| `utils/EsrequestUtil.js` | 同上 |
| `pages/index/index.vue` | 加载数据、搜索事件、广告跳转方法中各一处 |
| `pages/product/product.vue` | 未登录弹窗的取消回调中 |
| `pages/money/paySuccess.vue` | 打印订单号和支付状态 |
| `pages/search/search.vue` | 打印搜索结果和搜索失败 |
| `pages/brand/brandDetail.vue` | 未登录弹窗的取消回调中 |
| `pages/address/address.vue` | 打印页面参数和回调参数 |
| `pages/address/addressManage.vue` | 打印地址转化调试信息 |
| `pages/order/createOrder.vue` | 打印 `this.cartIds` 和取消回调 |
| `pages/order/order.vue` | 3 处取消回调中的调试日志 |
| `pages/order/orderDetail.vue` | 2 处取消回调中的调试日志 |
| `pages/public/register.vue` | 打印用户信息和授权失败信息 |
| `components/upload-images.vue` | 捕获上传错误后仅打印 `err` |

**改动方式：**
- 对于单纯的调试打印（如 `console.log("onLoad", response.data)`），直接删除整行。
- 对于空的取消回调（`else if (res.cancel) { console.log(...) }`），删除 `console.log` 行，保留 `else if` 分支结构（不影响逻辑，保持代码可读性）。
- 对于 `upload-images.vue` 中捕获上传错误后只打印的情况，替换为用户可见的错误提示：

```javascript
// 修改前
} catch(err) {
    console.log(err);
    return;
}

// 修改后
} catch(err) {
    uni.showToast({ title: '图片上传失败，请重试', icon: 'none', duration: 1500 });
    return;
}
```

**原因：** `console.log` 在生产包中仍会输出，可能暴露用户数据、接口结构等敏感信息；同时对用户而言无任何价值，是典型的调试遗留代码，必须清除。

---

### 2. 修复登录页明文存储密码的安全漏洞

**文件：** `pages/public/login.vue`

**问题：**
```javascript
// 修改前 — 将密码明文写入本地存储
uni.setStorageSync('token', token);
uni.setStorageSync('username', this.username);
uni.setStorageSync('password', this.password);  // ❌ 安全漏洞

// 修改前 — onLoad 中自动回填密码
this.password = uni.getStorageSync('password') || '';  // ❌ 读取明文密码
```

**修改后：**
```javascript
// 只保存 token 和 username（便于下次自动填写用户名）
uni.setStorageSync('token', token);
uni.setStorageSync('username', this.username);
// 不再存储密码

// onLoad 中只回填用户名
this.username = uni.getStorageSync('username') || '';
// 不再回填密码
```

**原因：** 将明文密码写入 `uni.setStorageSync`（等同于设备本地存储）是严重的安全漏洞：
- 设备被盗或被恶意软件访问时，密码将直接泄露；
- 用户换设备或清除缓存后，存储的密码也无法保护账号安全；
- Token 已经足够维持会话，无需再次存储密码用于重新登录。

---

### 3. 同时补充登录表单的前端输入校验

**文件：** `pages/public/login.vue`

**修改前：** 点击登录按钮后直接发起请求，空用户名/密码也会调用接口。

**修改后：** 提交前先做非空校验：
```javascript
async toLogin() {
    if (!this.username.trim()) {
        uni.showToast({ title: '请输入用户名', icon: 'none' });
        return;
    }
    if (!this.password) {
        uni.showToast({ title: '请输入密码', icon: 'none' });
        return;
    }
    // ...
}
```

**原因：** 前端校验可在不消耗网络请求的情况下，快速给用户反馈，避免空请求打到服务端，同时改善用户体验。

---

### 4. 修复登录流程嵌套 `.then()` 缺少内层错误处理

**文件：** `pages/public/login.vue`

**修改前：** 登录成功后调用 `memberInfo()` 获取用户信息，但内层 `.then()` 没有 `.catch()`，一旦获取用户信息失败，`logining` 状态永远不会重置为 `false`，登录按钮将永久禁用。

**修改后：**
```javascript
memberInfo().then(response => {
    this.login(response.data);
    uni.navigateBack();
}).catch(() => {
    this.logining = false;  // 内层失败时也正确重置按钮状态
});
```

**原因：** 任何异步操作失败时，UI 的 loading/disabled 状态都必须被正确重置，否则用户将陷入无法再次点击的死锁状态。

---

### 5. 完善 401 未登录处理：主动清除过期 Token

**文件：** `utils/requestUtil.js`、`utils/EsrequestUtil.js`

**修改前：** 收到 401 响应时，只弹出对话框询问是否重新登录，但**不清除本地已失效的 token**，导致后续请求仍会携带无效 token 继续失败。

**修改后：**
```javascript
if (res.code === 401) {
    uni.removeStorageSync('token');  // 立即清除过期 token
    uni.showModal({
        // ...
    });
}
```

同时移除了对话框取消按钮回调中无意义的 `console.log`，精简为只处理确认跳转的情况：
```javascript
success: function(res) {
    if (res.confirm) {
        uni.navigateTo({ url: '/pages/public/login' });
    }
    // 取消时不需要任何操作
}
```

**原因：** 保留无效 token 会导致后续所有需要认证的请求连续触发 401，形成错误循环。收到 401 时应立即清除 token，确保用户重新登录后获得干净的会话状态。

---

### 6. 修复响应错误拦截器的错误提示

**文件：** `utils/requestUtil.js`、`utils/EsrequestUtil.js`

**修改前：**
```javascript
}, (response) => {
    console.log('response error', response);
    uni.showToast({
        title: response.errMsg,  // ❌ 直接展示 luch-request 内部 errMsg，用户看不懂
        duration: 1500
    })
```

`response.errMsg` 是 luch-request 库内部的错误信息（如 `"request:fail"`），对用户毫无意义。

**修改后：**
```javascript
}, (response) => {
    uni.showToast({
        title: '网络请求失败，请检查网络连接',  // ✅ 用户友好的提示
        duration: 1500
    })
```

**原因：** 错误提示应对用户有实际指导意义；将内部技术错误码直接展示给用户是典型的开发者习惯遗留，应替换为清晰的中文提示。

---

### 7. 修复 Vuex `logout` mutation 中的异步存储调用不一致问题

**文件：** `store/index.js`

**修改前：**
```javascript
logout(state) {
    // ...
    uni.removeStorage({ key: 'userInfo' });   // 异步删除
    uni.removeStorage({ key: 'token' });       // 异步删除
}
```

**修改后：**
```javascript
logout(state) {
    // ...
    uni.removeStorage({ key: 'userInfo' });  // 保持异步（原有逻辑）
    uni.removeStorageSync('token');           // 同步确保立即清除
    uni.removeStorageSync('username');        // 同步确保立即清除
}
```

**原因：**
- 登出时 `token` 应同步清除，确保后续请求拦截器立即感知到已登出状态，而不是等待异步回调；
- 同时补充清除 `username`（因为登录时我们也存储了 `username`），保持数据清理完整性。

---

### 8. 修复 API 函数命名拼写错误

**文件：** `api/cart.js`、`pages/cart/cart.vue`

**修改前：**
```javascript
// api/cart.js
export function deletCartItem(params) { ... }  // ❌ 少了字母 'e'

// pages/cart/cart.vue
import { deletCartItem } from '@/api/cart.js'   // ❌ 同步错误
deletCartItem({ids: id}).then(...)              // ❌ 同步错误
```

**修改后：**
```javascript
// api/cart.js
export function deleteCartItem(params) { ... }  // ✅ 正确拼写

// pages/cart/cart.vue
import { deleteCartItem } from '@/api/cart.js'  // ✅ 同步更新
deleteCartItem({ids: id}).then(...)             // ✅ 同步更新
```

**原因：** 函数名是代码的对外契约，拼写错误会在后续维护或被其他模块引用时造成困惑，应遵循准确的英文命名。

---

### 9. 完善 `appConfig.js` 部署说明

**文件：** `utils/appConfig.js`

增加注释，明确说明如何在生产环境修改接口地址：

```javascript
/**
 * API 请求基础路径配置
 *
 * 开发环境：保持默认值即可（需在本地启动对应后端服务）
 * 生产环境：将两个 URL 修改为实际部署的服务器地址，并使用 HTTPS
 */
export const API_BASE_URL = 'http://localhost:8085';
export const esAPI_BASE_URL = 'http://localhost:8081';
```

**原因：** 硬编码的 `localhost` 地址会让首次部署的开发者感到困惑，明确的注释能显著降低上手成本。

---

## 优化总结

| # | 文件 | 改动类型 | 核心收益 |
|---|---|---|---|
| 1 | 多文件（16个）| 代码规范 | 清除 31 处调试 `console` 语句，防止信息泄露 |
| 2 | `login.vue` | **安全加固** | 移除明文密码存储，消除凭证泄露风险 |
| 3 | `login.vue` | 用户体验 | 添加提交前空值校验，减少无效请求 |
| 4 | `login.vue` | Bug 修复 | 内层 `.then()` 补充 `.catch()`，防止按钮永久禁用 |
| 5 | `requestUtil.js` / `EsrequestUtil.js` | Bug 修复 | 401 时主动清除 token，防止错误循环 |
| 6 | `requestUtil.js` / `EsrequestUtil.js` | 用户体验 | 错误提示改为用户可理解的中文 |
| 7 | `store/index.js` | Bug 修复 | `logout` 同步清除 token/username，确保立即生效 |
| 8 | `api/cart.js` / `cart.vue` | 代码规范 | 修复 `deletCartItem` → `deleteCartItem` 拼写错误 |
| 9 | `utils/appConfig.js` | 文档 | 补充生产环境部署说明 |
