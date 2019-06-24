package com.xmcc.service;

import com.xmcc.common.ResultResponse;
import com.xmcc.entity.ProductInfo;
import org.springframework.stereotype.Service;

@Service
public interface ProductInfoService {
     ResultResponse queryList();
    ResultResponse<ProductInfo> queryById(String productId);
    void updateProduct(ProductInfo productInfo);
}
