package com.xmcc.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import javax.validation.constraints.NotNull;

@Data
@ApiModel("订单列表参数信息")//swagger 参数的描述信息
public class OrderMasterPageDto {
    @NotNull(message = "订单列表Id不能为空")
    @ApiModelProperty(value = "订单列表Id",dataType = "String")
    private  String openid;
    @NotNull(message = "起始页")
    @ApiModelProperty(value = "从第几页开始",dataType = "Integer")
    private  Integer page=0;
    @NotNull(message = "一页显示几条不能为空")
    @ApiModelProperty(value = "一页显示几条",dataType = "Integer")
    private  Integer size=10;
}
