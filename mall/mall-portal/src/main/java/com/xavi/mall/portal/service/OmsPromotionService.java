package com.xavi.mall.portal.service;

import com.xavi.mall.model.OmsCartItem;
import com.xavi.mall.portal.domain.CartPromotionItem;

import java.util.List;

/**
 * 促销管理Service
 * Created by xavier
 */
public interface OmsPromotionService {
    /**
     * 计算购物车中的促销活动信息
     * @param cartItemList 购物车
     */
    List<CartPromotionItem> calcCartPromotion(List<OmsCartItem> cartItemList);
}
