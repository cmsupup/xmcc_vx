package com.xmcc.service;

import com.xmcc.entity.OrderDetail;

import java.util.List;

public interface OrderDetailSerice {

    // 批量插入
    void  batchInsert(List<OrderDetail> list);

    OrderDetail findOrderDetaisByorderId(String orderId);

}
