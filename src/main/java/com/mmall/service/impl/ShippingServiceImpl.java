package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServiceResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ServiceResponse add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int resultCount = shippingMapper.insert(shipping);
        if (resultCount == 0) {
            return ServiceResponse.createdByErrorMessage("新增地址失败");
        }
        Map result = Maps.newHashMap();
        result.put("shippingId",shipping.getId());
        return ServiceResponse.createdBySuccess("新增地址成功", result);
    }

    @Override
    public ServiceResponse del(Integer userId, Integer shippingId) {
        int resultCount = shippingMapper.delByShippingIdUserId(shippingId, userId);
        if (resultCount == 0) {
            return ServiceResponse.createdByErrorMessage("删除地址失败");
        }
        return ServiceResponse.createdBySuccess("删除地址成功");
    }

    @Override
    public ServiceResponse update(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int resultCount = shippingMapper.updateByShipping(shipping);
        if (resultCount == 0) {
            return ServiceResponse.createdByErrorMessage("更新地址失败");
        }
        return ServiceResponse.createdBySuccess("更新地址成功");
    }

    @Override
    public ServiceResponse select(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByShippingIdUserId(shippingId, userId);
        if (shipping == null) {
            return ServiceResponse.createdByErrorMessage("无法查询到该地址");
        }
        return ServiceResponse.createdBySuccess("查询地址成功", shipping);
    }

    @Override
    public ServiceResponse<PageInfo> list(Integer pageNum, Integer pageSize, Integer userId) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServiceResponse.createdBySuccess(pageInfo);
    }
}
