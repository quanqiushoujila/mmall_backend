package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServiceResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("iProductService")
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductMapper productMapper;

    /*后台部分*/
    /**
     * 新增OR更新产品
     * @param product
     * @return
     */
    @Override
    public ServiceResponse saveOrUpdateProduct(Product product) {
        if (product == null) {
            return ServiceResponse.createdByErrorMessage("新增或更新产品参数不正确");
        }
        if (StringUtils.isNotBlank(product.getSubImages())) {
            String[] subImagesArray = product.getSubImages().split(",");
            if (subImagesArray.length > 0) {
                product.setMainImage(subImagesArray[0]);
            }
        }
        if (product.getId() == null) {
            int resultCount = productMapper.insert(product);
            if (resultCount == 0) {
                return ServiceResponse.createdByErrorMessage("新增产品失败");
            }
            return ServiceResponse.createdBySuccess("新增产品成功");
        } else {
            int resultCount = productMapper.updateByPrimaryKey(product);
            if (resultCount == 0) {
                return ServiceResponse.createdByErrorMessage("更新产品失败");
            }
            return ServiceResponse.createdBySuccess("更新产品成功");
        }
    }

    /**
     * 产品上下架
     * @param productId
     * @param status
     * @return
     */
    @Override
    public ServiceResponse setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return ServiceResponse.createdByErrorMessage("修改产品状态参数不正确");
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);

        int resultCount = productMapper.updateByPrimaryKeySelective(product);
        if (resultCount == 0) {
            return ServiceResponse.createdByErrorMessage("修改产品状态成功");
        }
        return ServiceResponse.createdBySuccess("修改产品状态失败");
    }

    /**
     * 产品详情
     * @param productId
     * @return
     */
    @Override
    public ServiceResponse<Product> manageProductDetail(Integer productId) {
        if (productId == null) {
            return ServiceResponse.createdBySuccess("获取产品详情参数错误");
        }

        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return  ServiceResponse.createdByErrorMessage("产品已下架或者删除");
        }
        return ServiceResponse.createdBySuccess(product);
    }

    /**
     * 产品list
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ServiceResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize) {
        List<Product> productList = productMapper.selectList();

        PageHelper.startPage(pageNum, pageSize);
        List<ProductVo> productVoList = Lists.newArrayList();
        for(Product productItem : productList) {
            productVoList.add(assembleProductListVo(productItem));
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productVoList);
        return ServiceResponse.createdBySuccess(pageInfo);
    }

    /**
     * 产品搜索
     * @param productId
     * @param productName
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ServiceResponse<PageInfo> searchProduct(Integer productId, String productName, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(productName)) {
            productName = new StringBuffer().append("%").append(productName).append("%").toString();
        }
        List<Product> productList = productMapper.selectByProductIdProductName(productId, productName);
        List<ProductVo> productVoList = Lists.newArrayList();
        for(Product productItem : productList) {
            productVoList.add(assembleProductListVo(productItem));
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productVoList);
        return ServiceResponse.createdBySuccess(pageInfo);
    }

    public ProductVo assembleProductListVo(Product product) {
        ProductVo productVo = new ProductVo();
        productVo.setId(product.getId());
        productVo.setName(product.getName());
        productVo.setCategoryId(product.getCategoryId());
        productVo.setMainImage(product.getMainImage());
        productVo.setPrice(product.getPrice());
        productVo.setStatus(product.getStatus());
        productVo.setSubtitle(product.getSubtitle());
        return productVo;
    }



    /*前台部分*/

    /**
     * 获取商品详情
     * @param productId
     * @return
     */
    @Override
    public ServiceResponse<Product> productDetail(Integer productId) {
        if (productId == null) {
            return ServiceResponse.createdByeErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServiceResponse.createdByErrorMessage("产品已下架或者删除");
        }
        if (product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()) {
            return ServiceResponse.createdByErrorMessage("产品已下架或者删除");
        }
        return ServiceResponse.createdBySuccess(product);
    }

    /**
     * 产品搜索及动态排序List
     * @param categoryId
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    @Override
    public ServiceResponse<List<Product>> getProductByKeywordCategory(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy) {
        if (categoryId == null && StringUtils.isBlank(keyword)) {
            return ServiceResponse.createdByeErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
//        if ()
        PageHelper.startPage(pageNum, pageSize);

        return null;
    }
}
