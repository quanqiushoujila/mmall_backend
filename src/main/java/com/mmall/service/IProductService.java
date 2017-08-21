package com.mmall.service;

import com.mmall.common.ServiceResponse;
import com.mmall.pojo.Product;

import java.util.List;

public interface IProductService {
    //后台
    ServiceResponse saveOrUpdateProduct(Product product);

    ServiceResponse setSaleStatus(Integer productId, Integer status);

    ServiceResponse manageProductDetail(Integer productId);

    ServiceResponse<Product> getProductList(Integer pageNum,Integer pageSize);

    //前台
    ServiceResponse<Product> productDetail(Integer productId);

    ServiceResponse<List<Product>> getProductByKeywordCategory(Integer productId, String keyword, Integer pageNum, Integer pageSize, String orderBy);
}
