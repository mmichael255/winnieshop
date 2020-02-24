package com.winnie;

import com.winnie.common.utils.PageResult;
import com.winnie.item.ItemClient;
import com.winnie.item.dto.SpuDto;
import com.winnie.item.entity.SpecParam;
import com.winnie.search.entity.Goods;
import com.winnie.search.repository.SearchRepository;
import com.winnie.search.service.GoodsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class clienttest {
    @Autowired
    private ItemClient itemClient;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SearchRepository searchRepository;

    @Test
    public void Testshit(){
        List<SpecParam> param = itemClient.findParam(null, 76l, true);
        System.out.println(param);
    }

    @Test
    public void buildIndex(){
        int page = 1,pages=1;
        do {
            PageResult<SpuDto> pageResult = itemClient.pageSpu(page, 100, null, true);
            List<SpuDto> dataList = pageResult.getDataList();
            dataList.forEach(spuDto -> {
                Goods r = goodsService.spuDtoToGoods(spuDto);
                searchRepository.save(r);
            });
            pages = pageResult.getTotalPage();
            page++;
        }while (page<=pages);

    }
}
