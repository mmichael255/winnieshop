package com.winnie.item.mapper;

import com.winnie.item.entity.Sku;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

public interface SkuMapper extends BaseMapper<Sku>, InsertListMapper<Sku>, SelectByIdListMapper<Sku,Long> {
}
