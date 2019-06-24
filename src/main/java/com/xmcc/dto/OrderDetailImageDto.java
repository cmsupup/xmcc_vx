package com.xmcc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xmcc.entity.OrderDetail;
import com.xmcc.entity.OrderMaster;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderDetailImageDto extends OrderDetail implements Serializable {

    @JsonProperty("productImage")
    private String productImage=getProductIcon();

    //转换成Dto
    public static OrderDetailImageDto build(OrderDetail orderDetail) {
        OrderDetailImageDto orderDetailImageDto = new OrderDetailImageDto();
        BeanUtils.copyProperties(orderDetail, orderDetailImageDto);
        return orderDetailImageDto;
    }
}