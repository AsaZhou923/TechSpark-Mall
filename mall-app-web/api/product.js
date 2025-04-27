import request from '@/utils/requestUtil'

export function searchProductList(params) {
	return request({
		method: 'GET',
		url: '/product/search',
		params: params
	})
}

export function fetchCategoryTreeList() {
	return request({
		method: 'GET',
		url: '/product/categoryTreeList'
	})
}

export function fetchProductDetail(id) {
	return request({
		method: 'GET',
		url: '/product/detail/'+id
	})
}

/**
 * 综合搜索商品
 * @param {Object} params 搜索参数
 */
export function searchProducts(params) {
	return request({
		url: '/search',
		method: 'get',
		params: params
	});
}
