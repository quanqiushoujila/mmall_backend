package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServiceResponse;
import com.mmall.pojo.Shipping;

import java.util.Map;

public interface IShippingService {
    ServiceResponse add(Integer userId, Shipping shipping);

    ServiceResponse del(Integer userId, Integer shippingId);

    ServiceResponse update(Integer userId, Shipping shipping);

    ServiceResponse select(Integer userId, Integer shippingId);

    ServiceResponse<PageInfo> list(Integer pageNum, Integer pageSize, Integer userId);
}
