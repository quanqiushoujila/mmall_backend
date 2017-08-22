package com.mmall.dao;

import com.mmall.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    List<Cart> selectCartByUserId(Integer userId);

    Cart selectCartByProductIdUserId(@Param("productId") Integer productId,@Param("userId") Integer userId);

    int selectCartProductCheckedStatusByUserId(Integer userId);

    int deleteByUserIdProductIds(@Param("userId") Integer userId, @Param("productList") List<String> productList);

    int checkedOrUncheckedProduct(@Param("productId") Integer productId,@Param("userId") Integer userId, @Param("checked") Integer checked);

    int selectCartProductCount(Integer userId);
}