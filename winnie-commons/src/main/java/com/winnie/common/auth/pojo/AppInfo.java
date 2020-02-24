package com.winnie.common.auth.pojo;

import lombok.Data;

import java.util.List;

@Data
public class AppInfo {
    private Long id;
    private String serviceName;
    private List<Long> targetList;
}
