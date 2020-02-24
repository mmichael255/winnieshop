package com.winnie.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.netflix.discovery.converters.Auto;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.winnie.common.exception.pojo.ExceptionEnum;
import com.winnie.common.exception.pojo.WNException;
import com.winnie.common.utils.BeanHelper;
import com.winnie.common.utils.JsonUtils;
import com.winnie.common.utils.PageResult;
import com.winnie.item.ItemClient;
import com.winnie.item.dto.SpuDto;
import com.winnie.item.entity.*;
import com.winnie.search.dto.GoodsDto;
import com.winnie.search.dto.SearchRequest;
import com.winnie.search.entity.Goods;

import com.winnie.search.repository.SearchRepository;
import com.winnie.search.utills.HighlightUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.*;

import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SourceFilter;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoodsService {
    @Autowired
    private ItemClient itemClient;
    @Autowired
    private SearchRepository searchRepository;
    @Autowired
    private ElasticsearchTemplate elaTemplate;



    public Goods spuDtoToGoods(SpuDto spuDto){
        List<Sku> skuOfSpu = itemClient.getSkuOfSpu(spuDto.getId());
        List<Map<String,Object>> skuMap = new ArrayList<>();
        skuOfSpu.forEach(sku ->{
            Long id = sku.getId();
            String image = sku.getImages();
            String title = sku.getTitle().substring(spuDto.getName().length());
            Long price = sku.getPrice();
            HashMap<String, Object> skuHashMap = new HashMap<>();
            skuHashMap.put("id",id);
            skuHashMap.put("image",image);
            skuHashMap.put("title",title);
            skuHashMap.put("price",price);
            skuMap.add(skuHashMap);
        });
        String skuJson = JsonUtils.toString(skuMap);


        Set<Long> priceSet = skuOfSpu.stream().map(Sku::getPrice).collect(Collectors.toSet());

        SpuDetail spuDetail = itemClient.getSpuDetail(spuDto.getId());
        String genericSpec = spuDetail.getGenericSpec();
        String specialSpec = spuDetail.getSpecialSpec();
        Map<Long, Object> genericSpecMap = JsonUtils.toMap(genericSpec, Long.class, Object.class);
        Map<Long, List<Object>> specialSpecMap = JsonUtils.nativeRead(specialSpec, new TypeReference<Map<Long,List<Object>>>(){});
        List<SpecParam> param = itemClient.findParam(null, spuDto.getCid3(), true);
        HashMap<String, Object> paramMap = new HashMap<>();
        param.forEach(specParam -> {
            String name = specParam.getName();
            Object value = null;
            if (specParam.getGeneric()){
                value = genericSpecMap.get(specParam.getId());

            }else {
                value = specialSpecMap.get(specParam.getId());
            }
            if (specParam.getNumeric()){
                value = chooseSegment(value, specParam);;
            }
            paramMap.put(name,value);
        });
        Goods goods = new Goods();
        goods.setAll(spuDto.getCategoryName()+ spuDto.getBrandName());
        goods.setPrice(priceSet);
        goods.setSkus(skuJson);
        goods.setSpecs(paramMap);
        goods.setId(spuDto.getId());
        goods.setCategoryId(spuDto.getCid3());
        goods.setBrandId(spuDto.getBrandId());
        goods.setCreateTime(spuDto.getCreateTime());
        goods.setSpuName(spuDto.getName());
        goods.setSpuTitle(spuDto.getSubTitle());
        return goods;
    }

    private String chooseSegment(Object value, SpecParam p) {
        if (value == null || StringUtils.isBlank(value.toString())) {
            return "其它";
        }
        double val = parseDouble(value.toString());
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = parseDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = parseDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    private double parseDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return 0;
        }
    }

    public PageResult<GoodsDto> getGoodsPage(SearchRequest searchRequest) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","spuName","spuTitle","skus"},null));
        queryBuilder.withPageable(PageRequest.of(searchRequest.getPage() -1,searchRequest.getSize()));
        HighlightUtils.highlightField(queryBuilder,"spuName");
        queryBuilder.withQuery(getQueryBuilder(searchRequest));

        AggregatedPage<Goods> goods = elaTemplate.queryForPage(queryBuilder.build(), Goods.class,HighlightUtils.highlightBody(Goods.class,"spuName"));
        PageResult<GoodsDto> goodsPageResult = new PageResult<>(goods.getTotalElements(), goods.getTotalPages(), BeanHelper.copyWithCollection(goods.getContent(),GoodsDto.class));
        return goodsPageResult;

    }

    private QueryBuilder getQueryBuilder(SearchRequest searchRequest) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.multiMatchQuery(searchRequest.getKey(),"all","spuName").operator(Operator.AND));

        Map<String, Object> paramKey = searchRequest.getParamKey();
        Set<Map.Entry<String, Object>> entries = paramKey.entrySet();
        entries.forEach(stringObjectEntry -> {
            String key = stringObjectEntry.getKey();
            Object value = stringObjectEntry.getValue();
            if (StringUtils.equals(key, "分类")) {
                key = "categoryId";
            } else if (StringUtils.equals(key, "品牌")) {
                key = "brandId";
            } else {
                key = "specs."+key+".keyword";
            }
            boolQueryBuilder.filter(QueryBuilders.termQuery(key,value));
        });
        return boolQueryBuilder;
    }


    public Map<String, List<?>> getFilterData(SearchRequest searchRequest) {
        Map<String, List<?>> filterMap = new LinkedHashMap<>();
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{""},null));
        queryBuilder.withPageable(new PageRequest(0,1));
        queryBuilder.withQuery(getQueryBuilder(searchRequest));
        String categoryName = "categoryAgg";
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryName).field("categoryId"));
        String brandName = "brandAgg";
        queryBuilder.addAggregation(AggregationBuilders.terms(brandName).field("brandId"));
        AggregatedPage<Goods> goods = elaTemplate.queryForPage(queryBuilder.build(), Goods.class);
        Aggregations aggregations = goods.getAggregations();
        Terms categoryTerm = aggregations.get(categoryName);
        List<Long> cids = handlerCategoryTerm(filterMap, categoryTerm);


        Terms brandTerm = aggregations.get(brandName);
        handerBrandTerm(filterMap,brandTerm);

        handlerSpecTerm(filterMap,getQueryBuilder(searchRequest),cids);
        return filterMap;
    }

    private void handlerSpecTerm(Map<String, List<?>> filterMap, QueryBuilder queryBuilder, List<Long> cids) {
        cids.forEach(cid -> {
            List<SpecParam> params = itemClient.findParam(null, cid, true);
            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{""},null));
            nativeSearchQueryBuilder.withPageable(PageRequest.of(0,1));
            nativeSearchQueryBuilder.withQuery(queryBuilder);
            params.forEach(param -> {
                String paramName = param.getName();
                String fieldName = "specs."+paramName+".keyword";
                nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(paramName).field(fieldName));
            });

            AggregatedPage<Goods> goods = elaTemplate.queryForPage(nativeSearchQueryBuilder.build(), Goods.class);
            Aggregations aggregations = goods.getAggregations();

            params.forEach(param -> {
                String paramName = param.getName();
                Terms specTerms = aggregations.get(paramName);
                List<String> collect = specTerms.getBuckets().stream()
                        .map(Terms.Bucket::getKeyAsString)
                        .collect(Collectors.toList());

                filterMap.put(paramName,collect);
            });
        });
    }

    private void handerBrandTerm(Map<String, List<?>> filterMap, Terms brandTerm) {
        List<Brand> collect = brandTerm.getBuckets()
                .stream()
                .map(Terms.Bucket::getKeyAsNumber)
                .map(Number::longValue)
                .map(itemClient::findBrandById)
                .collect(Collectors.toList());
        filterMap.put("品牌",collect);
    }

    private List<Long> handlerCategoryTerm(Map<String, List<?>> filterMap, Terms categoryTerm) {
        List<Long> collect = categoryTerm.getBuckets()
                .stream()
                .map(Terms.Bucket::getKeyAsNumber)
                .map(Number::longValue)
                .collect(Collectors.toList());
        List<Category> categories = itemClient.findCategories(collect);
        filterMap.put("分类",categories);
        return collect;
    }

    public void addIndex(Long spuId){
        try {
            SpuDto spuDtoByspuId = itemClient.getSpuDtoByspuId(spuId);
            Goods goods = spuDtoToGoods(spuDtoByspuId);
            searchRepository.save(goods);
        } catch (Exception e) {
            throw new WNException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
    }

    public void deleIndex(Long spuId){
        try {
            searchRepository.deleteById(spuId);
        }catch (Exception e){
            throw new WNException(ExceptionEnum.DELETE_OPERATION_FAIL);
        }
    }
}
