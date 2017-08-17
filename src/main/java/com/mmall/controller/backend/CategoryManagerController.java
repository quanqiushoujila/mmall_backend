package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServiceResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/category")
public class CategoryManagerController {

    @Autowired
    private ICategoryService iCategoryService;
    @Autowired
    private IUserService iUserService;

    /**
     * 增加品类节点
     * @param session
     * @param categoryName
     * @param parentId
     * @return
     */
    @RequestMapping(value = "add_category.do")
    @ResponseBody
    public  ServiceResponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createdByErrorMessage("用户未登录，无法获取信息");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.AddCategory(categoryName, parentId);
        } else {
            return ServiceResponse.createdByErrorMessage("用户不是管理员，没有权限修改");
        }
    }

    @RequestMapping(value = "set_category_name.do")
    @ResponseBody
    public ServiceResponse setCategoryName(String categoryName, Integer categoryId, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createdByErrorMessage("用户未登录，无法获取信息");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.updateCategoryName(categoryName, categoryId);
        } else {
            return ServiceResponse.createdByErrorMessage("用户不是管理员，没有权限查看");
        }
    }

    @RequestMapping(value = "get_deep_category.do")
    @ResponseBody
    public ServiceResponse getChildrenParallelCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createdByErrorMessage("用户未登录,请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.selectCategoryAndChildrenById(categoryId);
        } else {
            return ServiceResponse.createdByErrorMessage("用户不是管理员，没有权限查看");
        }

    }

    @RequestMapping(value = "get_category.do")
    @ResponseBody
    public  ServiceResponse getCategoryAndDeepChildrenCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createdByeErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        }

        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.getCategoryChildrenByParentId(categoryId);
        }
        return ServiceResponse.createdByErrorMessage("用户不是管理员，没有权限查看");
    }




}
