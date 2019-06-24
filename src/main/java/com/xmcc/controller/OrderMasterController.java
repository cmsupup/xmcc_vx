package com.xmcc.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xmcc.common.ResultResponse;
import com.xmcc.dto.*;
import com.xmcc.entity.OrderDetail;
import com.xmcc.entity.OrderMaster;
import com.xmcc.service.OrderDetailSerice;
import com.xmcc.service.OrderMasterService;

import com.xmcc.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("buyer/order")
@Api(value = "订单相关接口",description = "完成订单的增删改查")
public class OrderMasterController {
    @Autowired
    private OrderMasterService orderMasterService;
    @Autowired
    private OrderDetailSerice  orderDetailSerice;


    //@Valid配合刚才在DTO上的JSR303注解完成校验
    //注意：JSR303的注解默认是在Contorller层进行校验
    //如果想在service层进行校验 需要使用javax.validation.Validator  也就是上个项目用到的工具
    @PostMapping("create")
    @ApiOperation(value = "创建订单接口", httpMethod = "POST", response = ResultResponse.class)
    public ResultResponse create(
            @Valid
            @ApiParam(name="订单对象",value = "传入json格式",required = true)
            OrderMasterDto orderMasterDto, BindingResult bindingResult){
        Map<String,String> map = Maps.newHashMap();
        //判断是否有参数校验问题
        if(bindingResult.hasErrors()){
            List<String> errList = bindingResult.getFieldErrors().stream().map(err -> err.getDefaultMessage()).collect(Collectors.toList());
            map.put("参数校验错误", JsonUtil.object2string(errList));
            //将参数校验的错误信息返回给前台
            return  ResultResponse.fail(map);
        }
        return orderMasterService.insertOrder(orderMasterDto);
    }


    @GetMapping("list")
    @ApiOperation(value = "订单列表")//使用swagger2的注解对方法接口描述
    public ResultResponse list(
            @ApiParam(name="订单列表分页参数",value = "传入json格式",required = true)
            OrderMasterPageDto orderMasterPageDto){

        List<OrderMasterListDto> pageable = orderMasterService.findOrderMastersByOpenIdPageable(orderMasterPageDto.getOpenid(), orderMasterPageDto);
       // String s = JsonUtil.object2string(pageable);
        return ResultResponse.success(pageable);
    }

    @GetMapping("detail")
    @ApiOperation(value = "查看订单详情列表")//使用swagger2的注解对方法接口描述
    public ResultResponse detail(
            @ApiParam(name="微信用户id，与订单项id",value = "传入json格式",required = true)
            OrderDetailparameter detailparameter
    ){
        List<OrderMasterListDto> masterListDtos = orderMasterService.findOrderMastersByOpenId(detailparameter.getOpenid());
        ArrayList<OrderDetailImageDto>  dtos = Lists.newArrayList();
        for (OrderMasterListDto o: masterListDtos
             ) {
                 OrderDetail orderDetail= orderDetailSerice.findOrderDetaisByorderId(detailparameter.getOrderId());
                 OrderDetailImageDto detailImageDto = OrderDetailImageDto.build(orderDetail);
                  dtos.add(detailImageDto);
            o.setOrderDetailImageDtos(dtos);
        }
        return ResultResponse.success(masterListDtos);
    }

    @PostMapping("cancel")
    @ApiOperation(value = "取消订单")//使用swagger2的注解对方法接口描述
    public ResultResponse cancel(
            @ApiParam(name="微信用户id，与订单项id",value = "传入json格式",required = true)
                    OrderDetailparameter detailparameter
    ){
             orderMasterService.updateMaterByOpenId(detailparameter);

    return ResultResponse.success();
    }
}
