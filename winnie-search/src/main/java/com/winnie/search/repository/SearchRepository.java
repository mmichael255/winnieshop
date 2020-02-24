package com.winnie.search.repository;

import com.winnie.search.entity.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SearchRepository extends ElasticsearchRepository<Goods,Long> {
}
