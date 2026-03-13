// appConfig.js

/**
 * API 请求基础路径配置
 *
 * 开发环境：保持以下默认值即可（需在本地启动对应后端服务）
 * 生产环境：将两个 URL 修改为实际部署的服务器地址，并使用 HTTPS
 *   例如：
 *     API_BASE_URL   = 'https://your-domain.com'
 *     esAPI_BASE_URL = 'https://your-domain.com:8081'
 */
export const API_BASE_URL = 'http://localhost:8085';
export const esAPI_BASE_URL = 'http://localhost:8081';
//是否启用支付宝支付
export const USE_ALIPAY = false;