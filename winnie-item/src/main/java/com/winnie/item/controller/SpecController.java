package com.winnie.item.controller;

import com.winnie.item.dto.SpecsDto;
import com.winnie.item.entity.SpecGroup;
import com.winnie.item.entity.SpecParam;
import com.winnie.item.service.SpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SpecController {
    @Autowired
    private SpecService specService;

    @GetMapping("/spec/groups/of/category")
    public ResponseEntity<List<SpecGroup>> findGroups(@RequestParam("id")Long id){
        List<SpecGroup> groupsByCategory = specService.findGroupsByCategory(id);
        return ResponseEntity.ok(groupsByCategory);
    }

    @GetMapping("/spec/params")
    public ResponseEntity<List<SpecParam>> findParam(@RequestParam(value = "gid",required = false)Long gid,
                                                     @RequestParam(value = "cid",required = false)Long cid,
                                                     @RequestParam(value = "searching",required = false)Boolean searching){
        List<SpecParam> params = specService.findParams(gid,cid,searching);
        return ResponseEntity.ok(params);
    }

    @GetMapping("/spec/groupsdto/of/category")
    public ResponseEntity<List<SpecsDto>> findSpecDtoByCategoryId(@RequestParam("id")Long id){
        List<SpecsDto> specsDtos = specService.findSpecDtoByCategoryId(id);
        return ResponseEntity.ok(specsDtos);
    }
}
