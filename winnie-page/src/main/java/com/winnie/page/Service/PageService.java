package com.winnie.page.Service;

import com.netflix.discovery.converters.Auto;
import com.winnie.common.exception.pojo.ExceptionEnum;
import com.winnie.common.exception.pojo.WNException;
import com.winnie.item.ItemClient;
import com.winnie.item.dto.SpecsDto;
import com.winnie.item.dto.SpuDto;
import com.winnie.item.entity.Brand;
import com.winnie.item.entity.Category;
import com.winnie.item.entity.SpecGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PageService {
    @Autowired
    private ItemClient itemClient;
    @Autowired
    private SpringTemplateEngine templateEngine;
    @Value("${wn.static.itemDir}")
    private String itemDir;
    @Value("${wn.static.itemTemplate}")
    private String itemTemplate;

    public Map<String, Object> loadItemPageData(Long spuId) {
        SpuDto spuDtoByspuId = itemClient.getSpuDtoByspuId(spuId);
        List<Category> categories = itemClient.findCategories(spuDtoByspuId.getCategoryIds());
        Brand brandById = itemClient.findBrandById(spuDtoByspuId.getBrandId());

        List<SpecsDto> specDtoByCategoryId = itemClient.findSpecDtoByCategoryId(spuDtoByspuId.getCid3());
        HashMap<String, Object> pageDataMap = new HashMap<>();
        pageDataMap.put("categories",categories);
        pageDataMap.put("brand",brandById);
        pageDataMap.put("spuName",spuDtoByspuId.getName());
        pageDataMap.put("subTitle",spuDtoByspuId.getSubTitle());
        pageDataMap.put("detail",spuDtoByspuId.getSpuDetail());

        pageDataMap.put("skus",spuDtoByspuId.getSkus());

        pageDataMap.put("specs",specDtoByCategoryId);
        return pageDataMap;
    }

    public void createItemStaticPage(Long spuId){
        Map<String, Object> stringObjectMap = loadItemPageData(spuId);
        Context context = new Context();
        context.setVariables(stringObjectMap);

        File file = new File(new File(itemDir), spuId + ".html");

        try (PrintWriter printWriter = new PrintWriter(file)){
            templateEngine.process(itemTemplate,context,printWriter);
        }catch (Exception e){
            throw new WNException(ExceptionEnum.FILE_WRITER_ERROR);
        }
    }

    public void deleStaticPage(Long id){
        File file = new File(new File(itemDir), id + ".html");
        if (file.exists()){
            file.delete();
        }
    }


}
