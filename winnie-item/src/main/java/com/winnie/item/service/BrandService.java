package com.winnie.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winnie.common.exception.pojo.ExceptionEnum;
import com.winnie.common.exception.pojo.WNException;
import com.winnie.common.utils.PageResult;
import com.winnie.item.entity.Brand;
import com.winnie.item.entity.Category;
import com.winnie.item.mapper.BrandMapper;
import com.winnie.item.mapper.CategoryMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
@Transactional
public class BrandService {
    @Autowired
    BrandMapper brandMapper;

    @Autowired
    CategoryMapper categoryMapper;

    public PageResult<Brand> selectBrandPageResult(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        PageHelper.startPage(page,rows);
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(key)){
            Example.Criteria criteria = example.createCriteria();
            criteria.orLike("name","%"+key+"%");
            criteria.andEqualTo("letter",key.toUpperCase());
        }
        if (StringUtils.isNotBlank(sortBy)){
            example.setOrderByClause(sortBy+" "+(desc?"desc":"asc"));
        }
        List<Brand> brands = brandMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(brands)){
            throw new WNException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);
        PageResult<Brand> pageResult = new PageResult<>(pageInfo.getTotal(), pageInfo.getPages(), brands);
        return pageResult;
    }

    public void insertBrandAndCategory(Brand brand, List<Long> cids) {
        try {
            brandMapper.insertSelective(brand);
            categoryMapper.insertBrandAndCategory(cids, brand.getId());
        }catch (Exception e){
            throw new WNException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
    }


    public Brand findBrandById(Long id) {
        Brand r = brandMapper.selectByPrimaryKey(id);
        if (r == null){
            throw new WNException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return r;
    }

    public List<Brand> findBrandOfCategory(Long cid) {
        List<Brand> brands = brandMapper.findBrandOfCategory(cid);
        return brands;
    }
}
