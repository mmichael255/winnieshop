package com.winnie.auth.mapper;

import com.winnie.auth.entity.ApplicationInfo;
import com.winnie.common.mapper.BaseMapper;

import java.util.List;

public interface ApplicationInfoMapper extends BaseMapper<ApplicationInfo> {
    List<Long> selectTargetIDOfApp(Long id);
}
