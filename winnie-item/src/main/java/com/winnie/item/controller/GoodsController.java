package com.winnie.item.controller;

import com.winnie.common.utils.PageResult;
import com.winnie.item.dto.SpuDto;
import com.winnie.item.entity.Sku;
import com.winnie.item.entity.SpuDetail;
import com.winnie.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.List;
import java.util.Map;

@RestController
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<SpuDto>> pageSpu(@RequestParam(defaultValue = "1",required = false)Integer page,
                                                      @RequestParam(defaultValue = "5",required = false)Integer rows,
                                                      @RequestParam(required = false)String key,
                                                      @RequestParam(required = false)Boolean saleable){
        PageResult<SpuDto> pageResult = goodsService.getPageSpu(page,rows,key,saleable);
        return ResponseEntity.ok(pageResult);
    }

    @PostMapping("/goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuDto spuDto){
        goodsService.saveGoods(spuDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/spu/saleable")
    public ResponseEntity<Void> setSaleable(@RequestParam("id")Long id,@RequestParam("saleable")Boolean saleable){
        goodsService.setSaleable(id,saleable);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/spu/detail")
    public ResponseEntity<SpuDetail> getSpuDetail(@RequestParam("id")Long id){
        SpuDetail spuDetail = goodsService.getSpuDetail(id);
        return ResponseEntity.ok(spuDetail);
    }

    @GetMapping("/sku/of/spu")
    public ResponseEntity<List<Sku>> getSkuOfSpu(@RequestParam("id")Long id){
        List<Sku> skus = goodsService.getSkuOfSpu(id);
        return ResponseEntity.ok(skus);
    }

    @PutMapping("/goods")
    public ResponseEntity<Void> updateGood(@RequestBody SpuDto spuDto){
        goodsService.updateGood(spuDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/spu/{id}")
    public ResponseEntity<SpuDto> getSpuDtoByspuId(@PathVariable("id")Long spuId){
        SpuDto spuDto = goodsService.getSpuDtoByspuId(spuId);
        return ResponseEntity.ok(spuDto);
    }

    @GetMapping("/sku/list")
    public ResponseEntity<List<Sku>> getSkuList(@RequestParam("ids")List<Long> ids){
        List<Sku> skus = goodsService.getSkuList(ids);
        return ResponseEntity.ok(skus);
    }

    @PutMapping("/stock/minus")
    public ResponseEntity<Void> minusStock(@RequestBody Map<Long, Integer> paramMap){
        goodsService.minusStock(paramMap);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
