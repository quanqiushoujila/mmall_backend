package com.mmall.service;

import com.mmall.common.ServiceResponse;
import com.mmall.pojo.Product;

public interface IProductService {
    ServiceResponse saveOrUpdateProduct(Product product);

    ServiceResponse setSaleStatus(Integer productId, Integer status);

    ServiceResponse manageProductDetail(Integer productId);
}
