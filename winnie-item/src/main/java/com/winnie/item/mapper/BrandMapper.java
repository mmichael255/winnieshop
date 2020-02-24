package com.winnie.item.mapper;


import com.winnie.item.entity.Brand;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand> {
    @Select("SELECT b.* FROM `tb_brand` b, `tb_category_brand` cb " +
            "WHERE b.`id` = cb.`brand_id` AND cb.`category_id` = #{cid}")
    List<Brand> findBrandOfCategory(Long cid);
}
