package com.winnie.order.mapper;

import com.winnie.common.mapper.BaseMapper;
import com.winnie.order.entity.OrderDetail;
import tk.mybatis.mapper.additional.insert.InsertListMapper;


import java.util.List;

public interface OrderDetailMapper extends BaseMapper<OrderDetail>, InsertListMapper<OrderDetail> {
}
