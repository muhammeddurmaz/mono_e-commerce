package com.fastshop.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.fastshop.domain.ProductType;
import com.fastshop.repository.ProductTypeRepository;
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
 * Spring Data Elasticsearch repository for the {@link ProductType} entity.
 */
public interface ProductTypeSearchRepository extends ElasticsearchRepository<ProductType, Long>, ProductTypeSearchRepositoryInternal {}

interface ProductTypeSearchRepositoryInternal {
    Page<ProductType> search(String query, Pageable pageable);

    Page<ProductType> search(Query query);

    void index(ProductType entity);
}

class ProductTypeSearchRepositoryInternalImpl implements ProductTypeSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final ProductTypeRepository repository;

    ProductTypeSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, ProductTypeRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<ProductType> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<ProductType> search(Query query) {
        SearchHits<ProductType> searchHits = elasticsearchTemplate.search(query, ProductType.class);
        List<ProductType> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(ProductType entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
