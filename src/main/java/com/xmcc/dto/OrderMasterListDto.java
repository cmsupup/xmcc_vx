package com.xmcc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xmcc.entity.OrderDetail;
import com.xmcc.entity.OrderMaster;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderMasterListDto extends OrderMaster implements Serializable {

    @JsonProperty("orderDetailList")
    public List<OrderDetailImageDto> orderDetailImageDtos;

    //转换成Dto
    public static OrderMasterListDto build(OrderMaster orderMaster) {
        OrderMasterListDto orderMasterListDto = new OrderMasterListDto();
        BeanUtils.copyProperties(orderMaster, orderMasterListDto);
        return orderMasterListDto;
    }
}