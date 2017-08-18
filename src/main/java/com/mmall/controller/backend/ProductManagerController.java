package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServiceResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/product")
public class ProductManagerController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;

    /**
     * 新增OR更新产品
     * @param session
     * @param product
     * @return
     */
    @RequestMapping(value = "save.do")
    @ResponseBody
    public ServiceResponse productSave(HttpSession session, Product product) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createdByeErrorCodeMessage(Const.Role.ROLE_ADMIN, "用户未登录,请登录管理员");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iProductService.saveOrUpdateProduct(product);
        }

        return ServiceResponse.createdByErrorMessage("无权限操作");
    }

    /**
     * 产品上下架
     * @param session
     * @param productId
     * @param status
     * @return
     */
    @RequestMapping(value = "set_sale_status.do")
    @ResponseBody
    public  ServiceResponse setSaleStatus(HttpSession session, Integer productId, Integer status) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createdByeErrorCodeMessage(Const.Role.ROLE_ADMIN, "用户未登录,请登录管理员");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iProductService.setSaleStatus(productId, status);
        }

        return ServiceResponse.createdByErrorMessage("无权限操作");
    }

    /**
     * 产品详情
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "detail.do")
    @ResponseBody
    public ServiceResponse getDetail(Integer productId, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createdByeErrorCodeMessage(Const.Role.ROLE_ADMIN, "用户未登录,请登录管理员");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iProductService.manageProductDetail(productId);
        }

        return ServiceResponse.createdByErrorMessage("无权限操作");
    }

    /**
     * 产品list
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "list.do")
    @ResponseBody
    public ServiceResponse getList(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createdByeErrorCodeMessage(Const.Role.ROLE_ADMIN, "用户未登录,请登录管理员");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iProductService.getProductList(pageNum,pageSize);
        }

        return ServiceResponse.createdByErrorMessage("无权限操作");
    }

}
