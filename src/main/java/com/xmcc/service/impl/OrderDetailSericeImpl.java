package com.xmcc.service.impl;

import com.xmcc.common.OrderEnum;
import com.xmcc.dao.impl.BatchDaoImpl;
import com.xmcc.entity.OrderDetail;
import com.xmcc.repository.OrderDetailRepository;
import com.xmcc.service.OrderDetailSerice;
import com.xmcc.util.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderDetailSericeImpl  extends BatchDaoImpl<OrderDetail> implements OrderDetailSerice {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    @Transactional
    public void batchInsert(List<OrderDetail> OrderDetail) {
         super.batchInsert(OrderDetail);
    }

    @Override
    public OrderDetail findOrderDetaisByorderId(String orderId) {
        return  orderDetailRepository.findAllByOrderIdIsNotIn(orderId);
    }


}
