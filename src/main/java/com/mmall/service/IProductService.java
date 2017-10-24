package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServiceResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductVo;

import java.util.List;

public interface IProductService {
    //后台
    ServiceResponse saveOrUpdateProduct(Product product);

    ServiceResponse setSaleStatus(Integer productId, Integer status);

    ServiceResponse manageProductDetail(Integer productId);

    ServiceResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize);

    ServiceResponse<PageInfo> searchProduct(Integer productId, String productName, Integer pageNum, Integer pageSize);

    //前台
    ServiceResponse<Product> productDetail(Integer productId);

    ServiceResponse<List<Product>> getProductByKeywordCategory(Integer productId, String keyword, Integer pageNum, Integer pageSize, String orderBy);
}
