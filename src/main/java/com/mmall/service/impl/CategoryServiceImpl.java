package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.Const;
import com.mmall.common.ServiceResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService{

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 增加品类节点
     * @param categoryName
     * @param parentId
     * @return
     */
    @Override
    public ServiceResponse AddCategory(String categoryName, Integer parentId) {
        if (StringUtils.isBlank(categoryName) || parentId == null) {
            return ServiceResponse.createdByErrorMessage("添加品类的参数错误");
        }

        Category category = new Category();
        category.setParentId(parentId);
        category.setName(categoryName);
        category.getStatus(true);
        int resultCount = categoryMapper.insert(category);
        if (resultCount == 0) {
            return ServiceResponse.createdByErrorMessage("添加品类失败");
        }
        return ServiceResponse.createdBySuccess("添加品类成功");
    }

    /**
     * 修改品类名字
     * @param categoryName
     * @param categoryId
     * @return
     */
    @Override
    public ServiceResponse updateCategoryName(String categoryName, Integer categoryId) {
        if (categoryId != null || StringUtils.isBlank(categoryName)) {
            return ServiceResponse.createdByErrorMessage("更新品类的参数错误");
        }

        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int resultCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (resultCount == 0) {
            return ServiceResponse.createdByErrorMessage("更新品类名字失败");
        }
        return ServiceResponse.createdBySuccess("更新品类名字成功");
    }

    /**
     * 获取品类子节点(平级)
     * @param categoryId
     * @return
     */
    @Override
    public ServiceResponse<List<Category>> getCategoryChildrenByParentId(Integer categoryId) {
        if (categoryId == null) {
            return ServiceResponse.createdByErrorMessage("获取品类的参数错误");
        }
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)) {
            logger.info("未找到当前分类的子分类");
        }
        return ServiceResponse.createdBySuccess(categoryList);
    }

    /**
     * 获取当前分类id及递归子节点categoryId
     * @param categoryId
     * @return
     */
    @Override
    public ServiceResponse selectCategoryAndChildrenById (Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        findChildrenById(categorySet, categoryId);

        List<Integer> categoryIdList = Lists.newArrayList();
        if (categorySet != null) {
            for (Category categoryItem : categorySet) {
                categoryIdList.add(categoryItem.getId());
            }
        }
        return ServiceResponse.createdBySuccess(categoryIdList);
    }

    /**
     * 子节点递归操作
     * @param categorySet
     * @param categoryId
     * @return
     */
    private Set<Category> findChildrenById (Set<Category> categorySet, Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }

        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category categoryItem : categoryList) {
            findChildrenById(categorySet, categoryItem.getId());
        }
        return categorySet;
    }

}
