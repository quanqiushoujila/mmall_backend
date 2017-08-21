package com.mmall.service;

import com.mmall.common.ServiceResponse;
import com.mmall.vo.CartVo;

public interface ICartService {
    ServiceResponse list(Integer userId);
    ServiceResponse<CartVo> add(Integer userId, Integer productId, Integer count);
}
