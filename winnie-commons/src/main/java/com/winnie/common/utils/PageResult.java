package com.winnie.common.utils;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PageResult<T> {
    private Long total;
    private Integer totalPage;
    private List<T> dataList;

    public PageResult() {
    }

    public PageResult(Long total, List<T> dataList) {
        this.total = total;
        this.dataList = dataList;
    }

    public PageResult(Long total, Integer totalPage, List<T> dataList) {
        this.total = total;
        this.totalPage = totalPage;
        this.dataList = dataList;
    }
}
