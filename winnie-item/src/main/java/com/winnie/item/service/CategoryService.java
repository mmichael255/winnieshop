package com.winnie.item.service;

import com.winnie.common.exception.pojo.ExceptionEnum;
import com.winnie.common.exception.pojo.WNException;
import com.winnie.item.entity.Category;
import com.winnie.item.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@Transactional
public class CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    public List<Category> selectAllById(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        List<Category> categoryList = categoryMapper.select(category);
        if (CollectionUtils.isEmpty(categoryList)){
            throw new WNException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return categoryList;
    }

    public List<Category> findCategories(List<Long> ids) {
        List<Category> categoryList = categoryMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(categoryList)){
            throw new WNException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return categoryList;
    }
}
