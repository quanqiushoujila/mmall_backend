package com.mmall.controller.backend;

import com.mmall.dao.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manage/product")
public class ProductManagerController {

    @Autowired
    private ProductMapper productMapper;
}
