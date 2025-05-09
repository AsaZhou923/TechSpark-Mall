import request from '@/utils/EsrequestUtil'

// 搜索商品
export function searchProducts(params) {
  return request({
    url: '/esProduct/search',
    method: 'get',
    params: params
  })
}