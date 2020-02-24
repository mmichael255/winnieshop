package com.winnie.item;

import com.winnie.common.utils.PageResult;
import com.winnie.item.dto.SpecsDto;
import com.winnie.item.dto.SpuDto;
import com.winnie.item.entity.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "item-service")
public interface ItemClient {

    @GetMapping("/spu/page")
    public PageResult<SpuDto> pageSpu(@RequestParam(defaultValue = "1",required = false)Integer page,
                                                      @RequestParam(defaultValue = "5",required = false)Integer rows,
                                                      @RequestParam(required = false)String key,
                                                      @RequestParam(required = false)Boolean saleable);

    @GetMapping("/sku/of/spu")
    public List<Sku> getSkuOfSpu(@RequestParam("id")Long id);

    @GetMapping("/spu/detail")
    public SpuDetail getSpuDetail(@RequestParam("id")Long id);

    @GetMapping("/spec/params")
    public List<SpecParam> findParam(@RequestParam(value = "gid",required = false)Long gid,
                                                     @RequestParam(value = "cid",required = false)Long cid,
                                                     @RequestParam(value = "searching",required = false)Boolean searching);
    @GetMapping("/category/list")
    public List<Category> findCategories(@RequestParam("ids")List<Long> ids);

    @GetMapping("/brand/{id}")
    public Brand findBrandById(@PathVariable("id")Long id);

    @GetMapping("/spu/{id}")
    public SpuDto getSpuDtoByspuId(@PathVariable("id")Long spuId);

    @GetMapping("/spec/groupsdto/of/category")
    public List<SpecsDto> findSpecDtoByCategoryId(@RequestParam("id")Long id);

    @PutMapping("/stock/minus")
    public Void minusStock(@RequestBody Map<Long, Integer> paramMap);

    @GetMapping("/sku/list")
    public List<Sku> getSkuList(@RequestParam("ids")List<Long> ids);
}
