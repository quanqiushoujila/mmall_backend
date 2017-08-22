package com.mmall.service;

import com.mmall.common.ServiceResponse;

public interface IOrderService {
    ServiceResponse createOrder(Integer userId, Integer shippingId);
}
