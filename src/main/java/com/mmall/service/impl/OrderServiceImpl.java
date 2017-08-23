package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServiceResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.OrderItemMapper;
import com.mmall.dao.OrderMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Order;
import com.mmall.service.IOrderService;
import com.mmall.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private CartMapper cartMapper;

    @Override
    public ServiceResponse createOrder(Integer userId, Integer shippingId) {
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        OrderVo orderVo = new OrderVo();

        return null;
    }
}
