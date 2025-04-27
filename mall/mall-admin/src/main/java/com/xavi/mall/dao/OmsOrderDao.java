package com.xavi.mall.dao;

import com.xavi.mall.dto.OmsOrderDeliveryParam;
import com.xavi.mall.dto.OmsOrderDetail;
import com.xavi.mall.dto.OmsOrderQueryParam;
import com.xavi.mall.model.OmsOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单查询自定义Dao
 * Created by xavier
 */
public interface OmsOrderDao {
    /**
     * 条件查询订单
     */
    List<OmsOrder> getList(@Param("queryParam") OmsOrderQueryParam queryParam);

    /**
     * 批量发货
     */
    int delivery(@Param("list") List<OmsOrderDeliveryParam> deliveryParamList);

    /**
     * 获取订单详情
     */
    OmsOrderDetail getDetail(@Param("id") Long id);
}
