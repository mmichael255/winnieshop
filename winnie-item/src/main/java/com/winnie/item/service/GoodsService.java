package com.winnie.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.netflix.discovery.converters.Auto;
import com.winnie.common.constants.MQConstant;
import com.winnie.common.exception.pojo.ExceptionEnum;
import com.winnie.common.exception.pojo.WNException;
import com.winnie.common.utils.BeanHelper;
import com.winnie.common.utils.PageResult;
import com.winnie.item.dto.SpuDto;
import com.winnie.item.entity.*;
import com.winnie.item.mapper.SkuMapper;
import com.winnie.item.mapper.SpuDetailMapper;
import com.winnie.item.mapper.SpuMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import sun.plugin.services.WNetscape4BrowserService;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class GoodsService {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private AmqpTemplate amqpTemplate;


    public PageResult<SpuDto> getPageSpu(Integer page, Integer rows, String key, Boolean saleable) {
        PageHelper.startPage(page,rows);
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(key)){
            criteria.andLike("name","%"+key+"%");
        }
        if (saleable != null){
            criteria.andEqualTo("saleable",saleable);
        }
        example.setOrderByClause("create_time desc");
        List<Spu> spus = spuMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(spus)){
            throw new WNException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        PageInfo<Spu> spuPageInfo = new PageInfo<>(spus);
        List<SpuDto> spuDtos = BeanHelper.copyWithCollection(spuPageInfo.getList(), SpuDto.class);
        handlerCategoryNameAndBrandName(spuDtos);
        PageResult<SpuDto> pageResult = new PageResult<>(spuPageInfo.getTotal(),
                spuPageInfo.getPages(),
                spuDtos);
        return pageResult;
    }

    private void handlerCategoryNameAndBrandName(List<SpuDto> spuDtos) {
        spuDtos.forEach(spuDto -> {
            List<Category> categories = categoryService.findCategories(spuDto.getCategoryIds());
            String categoryName = categories.stream()
                    .map(Category::getName)
                    .collect(Collectors.joining("/"));
            spuDto.setCategoryName(categoryName);

            spuDto.setBrandName(brandService.findBrandById(spuDto.getBrandId()).getName());
        });
    }

    public void saveGoods(SpuDto spuDto) {
        try {
            Spu spu = BeanHelper.copyProperties(spuDto, Spu.class);
            spu.setSaleable(false);
            spuMapper.insertSelective(spu);
            Long spuId = spu.getId();
            SpuDetail spuDetail = spuDto.getSpuDetail();
            spuDetail.setSpuId(spuId);
            spuDetailMapper.insertSelective(spuDetail);

            List<Sku> skus = spuDto.getSkus();
            skus.forEach(sku -> {
                sku.setSpuId(spuId);
                sku.setCreateTime(new Date());
                sku.setUpdateTime(new Date());
            });
            skuMapper.insertList(skus);
        }catch (Exception e){
            throw new WNException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
    }

    public void setSaleable(Long id, Boolean saleable) {
        try{
            Spu spu = new Spu();
            spu.setId(id);
            spu.setSaleable(saleable);
            spuMapper.updateByPrimaryKeySelective(spu);

            amqpTemplate.convertAndSend(MQConstant.Exchange.ITEM_EXCHANGE_NAME,
                    saleable?MQConstant.RoutingKey.ITEM_UP_KEY:MQConstant.RoutingKey.ITEM_DOWN_KEY,
                    id);

        }catch (Exception e){
            throw new WNException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }
    }

    public SpuDetail getSpuDetail(Long id) {
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(id);
        if (spuDetail == null){
            throw new WNException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        return spuDetail;
    }

    public List<Sku> getSkuOfSpu(Long id) {
        Sku sku = new Sku();
        sku.setSpuId(id);
        List<Sku> select = skuMapper.select(sku);
        if (CollectionUtils.isEmpty(select)){
            throw new WNException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        return select;
    }

    public void updateGood(SpuDto spuDto) {
        if (spuDto == null){
            throw new WNException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }
        try{
            Spu spu = BeanHelper.copyProperties(spuDto, Spu.class);
            spuMapper.updateByPrimaryKeySelective(spu);

            spuDetailMapper.updateByPrimaryKeySelective(spuDto.getSpuDetail());
            Long spuId = spuDto.getId();
            Sku skuDele = new Sku();
            skuDele.setSpuId(spuId);
            skuMapper.delete(skuDele);
            List<Sku> skus = spuDto.getSkus();
            skus.forEach(sku -> {
                sku.setSpuId(spuId);
                sku.setCreateTime(new Date());
                sku.setUpdateTime(new Date());
            });
            skuMapper.insertList(skus);
        }catch (Exception e){
            throw new WNException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }
    }

    public SpuDto getSpuDtoByspuId(Long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        if (spu == null){
            throw new WNException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        SpuDto spuDto = BeanHelper.copyProperties(spu, SpuDto.class);
        SpuDetail spuDetail = getSpuDetail(spuId);
        List<Sku> skuOfSpu = getSkuOfSpu(spuId);
        String categoriesCollect = categoryService.findCategories(spuDto.getCategoryIds()).stream()
                .map(Category::getName)
                .collect(Collectors.joining("/"));
        Brand brandById = brandService.findBrandById(spuDto.getBrandId());
        spuDto.setCategoryName(categoriesCollect);
        spuDto.setBrandName(brandById.getName());
        spuDto.setSpuDetail(spuDetail);
        spuDto.setSkus(skuOfSpu);

        return spuDto;
    }

    public List<Sku> getSkuList(List<Long> ids) {
        List<Sku> skus = skuMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(skus)){
            throw new WNException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        return skus;
    }

    public void minusStock(Map<Long, Integer> paramMap) {
        paramMap.entrySet().forEach(entry -> {
            Long skuID = entry.getKey();
            Sku sku = skuMapper.selectByPrimaryKey(skuID);
            Integer num = entry.getValue();

            int stock = sku.getStock() - num;
            Sku sku1 = new Sku();
            sku1.setId(skuID);
            sku1.setStock(stock);
            skuMapper.updateByPrimaryKeySelective(sku1);
        });
    }
}
