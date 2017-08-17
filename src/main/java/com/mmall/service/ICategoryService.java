package com.mmall.service;

import com.mmall.common.ServiceResponse;
import com.mmall.pojo.Category;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ICategoryService {
    ServiceResponse<List<Category>> getCategoryChildrenByParentId(Integer categoryId);

    ServiceResponse AddCategory(String categoryName, Integer parentId);

    ServiceResponse updateCategoryName(String categoryName, Integer parentId);

    ServiceResponse selectCategoryAndChildrenById(Integer categoryId);
}
