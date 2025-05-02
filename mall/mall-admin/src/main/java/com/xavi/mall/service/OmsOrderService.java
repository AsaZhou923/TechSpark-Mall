package com.xavi.mall.service;

import com.xavi.mall.dto.*;
import com.xavi.mall.model.OmsOrder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.math.BigDecimal;

/**
 * 订单管理Service
 * Created by xavier
 */
public interface OmsOrderService {
    /**
     * 分页查询订单
     */
    List<OmsOrder> list(OmsOrderQueryParam queryParam, Integer pageSize, Integer pageNum);

    /**
     * 批量发货
     */
    @Transactional
    int delivery(List<OmsOrderDeliveryParam> deliveryParamList);

    /**
     * 批量关闭订单
     */
    @Transactional
    int close(List<Long> ids, String note);

    /**
     * 批量删除订单
     */
    int delete(List<Long> ids);

    /**
     * 获取指定订单详情
     */
    OmsOrderDetail detail(Long id);

    /**
     * 修改订单收货人信息
     */
    @Transactional
    int updateReceiverInfo(OmsReceiverInfoParam receiverInfoParam);

    /**
     * 修改订单费用信息
     */
    @Transactional
    int updateMoneyInfo(OmsMoneyInfoParam moneyInfoParam);

    /**
     * 修改订单备注
     */
    @Transactional
    int updateNote(Long id, String note, Integer status);
    
    /**
     * 获取订单总数
     */
    long getOrderCount();
    
    /**
     * 获取销售总额
     */
    BigDecimal getSalesAmount();
}
