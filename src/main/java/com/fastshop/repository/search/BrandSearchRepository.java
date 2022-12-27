package com.fastshop.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.fastshop.domain.Brand;
import com.fastshop.repository.BrandRepository;
import java.util.List;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Data Elasticsearch repository for the {@link Brand} entity.
 */
public interface BrandSearchRepository extends ElasticsearchRepository<Brand, Long>, BrandSearchRepositoryInternal {}

interface BrandSearchRepositoryInternal {
    Page<Brand> search(String query, Pageable pageable);

    Page<Brand> search(Query query);

    void index(Brand entity);
}

class BrandSearchRepositoryInternalImpl implements BrandSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final BrandRepository repository;

    BrandSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, BrandRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Brand> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<Brand> search(Query query) {
        SearchHits<Brand> searchHits = elasticsearchTemplate.search(query, Brand.class);
        List<Brand> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Brand entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
