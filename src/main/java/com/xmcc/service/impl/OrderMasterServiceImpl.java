package com.xmcc.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xmcc.common.*;
import com.xmcc.dto.*;
import com.xmcc.entity.OrderDetail;
import com.xmcc.entity.OrderMaster;
import com.xmcc.entity.ProductInfo;
import com.xmcc.repository.OrderMasterRepository;
import com.xmcc.service.OrderDetailSerice;
import com.xmcc.service.OrderMasterService;
import com.xmcc.service.ProductInfoService;
import com.xmcc.util.BigDecimalUtil;
import com.xmcc.util.CustomException;
import com.xmcc.util.IDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderMasterServiceImpl implements OrderMasterService {
    @Autowired
    private ProductInfoService productInfoService;
    @Autowired
    private OrderDetailSerice orderDetailSerice;
    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Override
    @Transactional
    public ResultResponse insertOrder(OrderMasterDto orderMasterDto) {
        List<OrderDetailDto> items = orderMasterDto.getItems();
        List<OrderDetail> orderDetailList= Lists.newArrayList();
        BigDecimal totalPrice = new BigDecimal("0");
        for (OrderDetailDto orderDetailDto:items
             ) {
            ResultResponse<ProductInfo> productInfoResultResponse = productInfoService.queryById(orderDetailDto.getProductId());
            if(productInfoResultResponse.getCode()== ResultEnums.FAIL.getCode()){
                throw  new CustomException(productInfoResultResponse.getMsg());
            }
            ProductInfo productInfo = productInfoResultResponse.getData();
            if(productInfo.getProductStock()<orderDetailDto.getProductQuantity()){
                throw  new CustomException(ProductEnums.PRODUCT_NOT_ENOUGH.getMsg());
            }
            //将前台传入的订单项DTO与数据库查询到的 商品数据组装成OrderDetail 放入集合中  @builder
            OrderDetail orderDetail = OrderDetail.builder()
                    .detailId(IDUtils.createIdbyUUID())
                    .productIcon(productInfo.getProductIcon())
                    .productId(orderDetailDto.getProductId())
                    .productName(productInfo.getProductName())
                    .productPrice(productInfo.getProductPrice())
                    .productQuantity(orderDetailDto.getProductQuantity())
                    .build();
            orderDetailList.add(orderDetail);
            //减少商品库存
            productInfo.setProductStock(productInfo.getProductStock()-orderDetailDto.getProductQuantity());
            //修改库存
            productInfoService.updateProduct(productInfo);
            //计算价格
            totalPrice= BigDecimalUtil.add(totalPrice,BigDecimalUtil.multi(productInfo.getProductPrice(),orderDetailDto.getProductQuantity()));

        }
        //生成订单id
        String orderId = IDUtils.createIdbyUUID();
        //构建订单信息  日期等都用默认的即可
        OrderMaster orderMaster = OrderMaster.builder()
                .buyerAddress(orderMasterDto.getAddress())
                .buyerName(orderMasterDto.getName())
                .buyerOpenid(orderMasterDto.getOpenid())
                .orderStatus(OrderEnum.NEW.getCode())
                .payStatus(PayEnum.WAIT.getCode())
                .buyerPhone(orderMasterDto.getPhone())
                .orderId(orderId)
                .orderAmount(totalPrice)
                .build();
        //将生成的订单id，设置到订单项中
        List<OrderDetail> detailList = orderDetailList.stream().map(orderDetail -> {
            orderDetail.setOrderId(orderId);
            return orderDetail;
        }).collect(Collectors.toList());
        //插入订单项
        orderDetailSerice.batchInsert(detailList);
        //插入订单
        orderMasterRepository.save(orderMaster);
        HashMap<String, String> map = Maps.newHashMap();
        //按照前台要求的数据结构传入
        map.put("orderId",orderId);
        return ResultResponse.success(map);
    }

    @Override
    public List<OrderMasterListDto> findOrderMastersByOpenIdPageable(String openid, OrderMasterPageDto orderMasterPageDto) {

        Pageable pageable = new PageRequest(orderMasterPageDto.getPage(),orderMasterPageDto.getSize());
        Page<OrderMaster> masters = orderMasterRepository.findOrderMastersByBuyerOpenid(openid, pageable);
        if(masters.isEmpty()){
            throw new  CustomException(OrderEnum.OPENID_ERROR.getMsg());
        }
        List<OrderMaster> masterList = masters.getContent();

        List<OrderMaster> list = masterList.stream().filter(orderMaster -> orderMaster.getOrderStatus() != 2).collect(Collectors.toList());
        ArrayList<OrderMasterListDto> rderMasterListDtos = Lists.newArrayList();
        for (OrderMaster o:list
             ) {
            OrderMasterListDto build = OrderMasterListDto.build(o);
            build.setOrderDetailImageDtos(null);
            rderMasterListDtos.add(build);
        }

        return rderMasterListDtos;
    }

    @Override
    public List<OrderMasterListDto> findOrderMastersByOpenId(String openid) {
        List<OrderMaster> allByOrderId = orderMasterRepository.findAllByBuyerOpenid(openid);
        ArrayList<OrderMasterListDto> orderMasterListDtos = Lists.newArrayList();
        for (OrderMaster o: allByOrderId ) {
            OrderMasterListDto build = OrderMasterListDto.build(o);
            orderMasterListDtos.add(build);
        }

        return  orderMasterListDtos;
    }

    @Override
    @Transactional
    public void updateMaterByOpenId(OrderDetailparameter detailparameter) {
        if(detailparameter.equals("")&&detailparameter==null){
            throw new CustomException(OrderEnum.ORDER_NOT_EXITS.getMsg());
        }
        Optional<OrderMaster> byId = orderMasterRepository.findById(detailparameter.getOrderId());
        if(!byId.isPresent()){
          throw  new ClassCastException(OrderEnum.ORDER_NOT_EXITS.getMsg());
        }
        OrderMaster orderMaster = byId.get();
        orderMaster.setOrderStatus(2);
        orderMasterRepository.save(orderMaster);
    }


}
