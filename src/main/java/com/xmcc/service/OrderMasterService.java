package com.xmcc.service;

import com.xmcc.common.ResultResponse;
import com.xmcc.dto.OrderDetailparameter;
import com.xmcc.dto.OrderMasterDto;
import com.xmcc.dto.OrderMasterListDto;
import com.xmcc.dto.OrderMasterPageDto;


import java.util.List;

public interface OrderMasterService {
    ResultResponse insertOrder(OrderMasterDto orderMasterDto);
    List<OrderMasterListDto> findOrderMastersByOpenIdPageable(String openid, OrderMasterPageDto orderMasterPageDto);
    List<OrderMasterListDto>  findOrderMastersByOpenId(String openid);
    void updateMaterByOpenId(OrderDetailparameter detailparameter);
}
