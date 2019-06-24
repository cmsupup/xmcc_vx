package com.xmcc.common;

import lombok.Getter;

@Getter
public enum ProductEnums {
    PRODUCT_NOT_ENOUGH(10,"商品库存不足"),
    PARAM_ERROR(1,"参数异常"),
    NOT_EXITS(1,"商品不存在");
    private int code;
    private String msg;
    ProductEnums(int code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
