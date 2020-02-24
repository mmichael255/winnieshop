package com.winnie.item.mapper;

import com.winnie.common.mapper.BaseMapper;
import com.winnie.item.entity.Category;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


public interface CategoryMapper extends BaseMapper<Category> {
    void insertBrandAndCategory(@Param("cids") List<Long> cids, @Param("bid") Long id);
}
