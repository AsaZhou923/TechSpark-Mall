package com.xavi.mall.portal.domain;

import com.xavi.mall.model.PmsProduct;
import com.xavi.mall.model.PmsProductFullReduction;
import com.xavi.mall.model.PmsProductLadder;
import com.xavi.mall.model.PmsSkuStock;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 促销商品信息，包括sku、打折优惠、满减优惠
 * Created by xavier
 */
@Getter
@Setter
public class PromotionProduct extends PmsProduct {
    //商品库存信息
    private List<PmsSkuStock> skuStockList;
    //商品打折信息
    private List<PmsProductLadder> productLadderList;
    //商品满减信息
    private List<PmsProductFullReduction> productFullReductionList;
}
