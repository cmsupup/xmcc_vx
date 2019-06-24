package com.xmcc.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("订单详情参数")//swagger 参数的描述信息
public class OrderDetailparameter implements Serializable {
    @NotBlank(message = "微信用户id不能为空")
    @ApiModelProperty(value = "微信用户id",dataType = "String")//swagger 参数的描述信息
    private String openid;
    @NotNull(message = "订单项id不能为空")
    @ApiModelProperty(value = "订单项id",dataType = "String")
    private String orderId;
}
