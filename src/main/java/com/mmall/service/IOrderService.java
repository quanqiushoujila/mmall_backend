package com.mmall.service;

import com.mmall.common.ServiceResponse;

public interface IOrderService {
    ServiceResponse create(Integer userId, Integer shippingId);
}
