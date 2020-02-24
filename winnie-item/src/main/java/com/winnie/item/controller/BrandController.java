package com.winnie.item.controller;

import com.winnie.common.utils.PageResult;
import com.winnie.item.entity.Brand;
import com.winnie.item.mapper.BrandMapper;
import com.winnie.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.List;

@RestController
public class BrandController {
    @Autowired
    BrandService brandService;

    @GetMapping("/brand/page")
    public ResponseEntity<PageResult> selectBrandPageResult(@RequestParam(value = "key",required = false)String key,
                                                            @RequestParam(value = "page",defaultValue = "1")Integer page,
                                                            @RequestParam(value = "rows",defaultValue = "10")Integer rows,
                                                            @RequestParam(value = "sortBy",required = false)String sortBy,
                                                            @RequestParam(value = "desc",defaultValue = "false")Boolean desc){
        PageResult<Brand> pageResult = brandService.selectBrandPageResult(key,page,rows,sortBy,desc);
        return ResponseEntity.ok(pageResult);
    }

    @PostMapping("/brand")
    public ResponseEntity<Void> insertBrandAndCategory(Brand brand,@RequestParam("cids")List<Long> cids){
        brandService.insertBrandAndCategory(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/brand/{id}")
    public ResponseEntity<Brand> findBrandById(@PathVariable("id")Long id){
        Brand brand = brandService.findBrandById(id);
        return ResponseEntity.ok(brand);
    }

    @GetMapping("/brand/of/category")
    public ResponseEntity<List<Brand>> findBrandOfCategory(@RequestParam(value = "id",required = true)Long cid){
        List<Brand> brands = brandService.findBrandOfCategory(cid);
        return ResponseEntity.ok(brands);
    }

}
