package com.winnie.item.controller;

import com.winnie.item.entity.Category;
import com.winnie.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/category/of/parent")
    public ResponseEntity<List<Category>> selectAllByPid(@RequestParam Long pid){
        List<Category> categoryList = categoryService.selectAllById(pid);
//        return ResponseEntity.status(HttpStatus.OK).body(categoryList);

        return ResponseEntity.ok(categoryList);
    }

    @GetMapping("/category/list")
    public ResponseEntity<List<Category>> findCategories(@RequestParam("ids")List<Long> ids){
        List<Category> categoryList = categoryService.findCategories(ids);
        return ResponseEntity.ok(categoryList);
    }
}
