package com.winnie.search.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.Map;
import java.util.Set;

@Data
@Document(indexName = "goods",type = "docs",shards = 1,replicas = 1)
public class Goods {
    @Id
    @Field(type = FieldType.Keyword)
    private Long id;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String spuName;
    @Field(type = FieldType.Keyword,index = false)
    private String spuTitle;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String all;
    @Field(type = FieldType.Keyword)
    private String skus;

    private Long categoryId;
    private Long brandId;
    private Map<String,Object> specs;
    private Date createTime;
    private Set<Long> price;
}
