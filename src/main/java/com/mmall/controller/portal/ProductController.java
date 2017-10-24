package com.mmall.controller.portal;

import com.mmall.common.ServiceResponse;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IUserService iUserService;

    /**
     * 产品详情
     * @param productId
     * @return
     */
    @RequestMapping(value = "detail.do")
    @ResponseBody
    public ServiceResponse<Product> detail(Integer productId) {
        return iProductService.productDetail(productId);
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
    @RequestMapping(value = "list.do")
    @ResponseBody
    public ServiceResponse<List<Product>> list(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                                               @RequestParam(value = "keyword", required = false) String keyword,
                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                               @RequestParam(value = "orderBy", defaultValue = "") String orderBy) {
        return null;
    }
}
