package com.winnie.search.controller;

import com.winnie.common.utils.PageResult;
import com.winnie.search.dto.GoodsDto;
import com.winnie.search.dto.SearchRequest;
import com.winnie.search.entity.Goods;
import com.winnie.search.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class SearchController {
    @Autowired
    private GoodsService goodsService;

    @PostMapping("/page")
    public ResponseEntity<PageResult<GoodsDto>> getGoodsPage(@RequestBody SearchRequest searchRequest){
        PageResult<GoodsDto> goodsDtoPageResult = goodsService.getGoodsPage(searchRequest);
        return ResponseEntity.ok(goodsDtoPageResult);
    }

    @PostMapping("/filter")
    public ResponseEntity<Map<String, List<?>>> getFilterData(@RequestBody SearchRequest searchRequest){
        Map<String, List<?>> filterData = goodsService.getFilterData(searchRequest);
        return ResponseEntity.ok(filterData);
    }
}
