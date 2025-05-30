package com.xavi.mall.portal.service;

import com.xavi.mall.model.PmsProduct;
import com.xavi.mall.portal.domain.PmsPortalProductDetail;
import com.xavi.mall.portal.domain.PmsProductCategoryNode;

import java.util.List;

/**
 * 前台商品管理Service
 * Created by xavier
 */
public interface PmsPortalProductService {
    /**
     * 综合搜索商品
     */
    List<PmsProduct> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize, Integer sort);

    /**
     * 以树形结构获取所有商品分类
     */
    List<PmsProductCategoryNode> categoryTreeList();

    /**
     * 获取前台商品详情
     */
    PmsPortalProductDetail detail(Long id);
}
