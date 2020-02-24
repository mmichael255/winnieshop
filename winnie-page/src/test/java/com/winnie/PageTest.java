package com.winnie;

import com.winnie.page.Service.PageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PageTest {
    @Autowired
    private PageService pageService;
    @Test
    public void createStaticPage(){
        pageService.createItemStaticPage(81L);
    }
}
