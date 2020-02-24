package com.winnie.item.dto;

import com.winnie.item.entity.SpecParam;
import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class SpecsDto {
    private Long id;

    private Long cid;

    private String name;

    private Date createTime;

    private Date updateTime;

    private List<SpecParam> params;
}
