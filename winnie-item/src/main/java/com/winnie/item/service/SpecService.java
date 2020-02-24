package com.winnie.item.service;

import com.winnie.common.exception.pojo.ExceptionEnum;
import com.winnie.common.exception.pojo.WNException;
import com.winnie.common.utils.BeanHelper;
import com.winnie.item.dto.SpecsDto;
import com.winnie.item.entity.SpecGroup;
import com.winnie.item.entity.SpecParam;
import com.winnie.item.mapper.SpecGroupMapper;
import com.winnie.item.mapper.SpecParamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class SpecService {
    @Autowired
    private SpecGroupMapper specGroupMapper;
    @Autowired
    private SpecParamMapper specParamMapper;


    public List<SpecGroup> findGroupsByCategory(Long id) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(id);
        return specGroupMapper.select(specGroup);
    }

    public List<SpecParam> findParams(Long gid, Long cid, Boolean searching) {
        if (gid == null && cid == null){
            throw new WNException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        SpecParam specParam = new SpecParam();
        specParam.setCid(cid);
        specParam.setGroupId(gid);
        specParam.setSearching(searching);
        List<SpecParam> params = specParamMapper.select(specParam);
        if (CollectionUtils.isEmpty(params)){
            throw new WNException(ExceptionEnum.SPEC_NOT_FOUND);
        }
        return params;
    }

    public List<SpecsDto> findSpecDtoByCategoryId(Long id) {
        List<SpecGroup> groupsByCategory = findGroupsByCategory(id);
        List<SpecsDto> specsDtos = BeanHelper.copyWithCollection(groupsByCategory, SpecsDto.class);
        List<SpecParam> params = findParams(null, id, null);
        Map<Long, List<SpecParam>> collect = params.stream().collect(Collectors.groupingBy(SpecParam::getGroupId));
        specsDtos.forEach(specsDto -> {
            specsDto.setParams(collect.get(specsDto.getId()));
        });

        return specsDtos;
    }
}
