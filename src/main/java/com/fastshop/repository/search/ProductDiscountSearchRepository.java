package com.fastshop.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.fastshop.domain.ProductDiscount;
import com.fastshop.repository.ProductDiscountRepository;
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
 * Spring Data Elasticsearch repository for the {@link ProductDiscount} entity.
 */
public interface ProductDiscountSearchRepository
    extends ElasticsearchRepository<ProductDiscount, Long>, ProductDiscountSearchRepositoryInternal {}

interface ProductDiscountSearchRepositoryInternal {
    Page<ProductDiscount> search(String query, Pageable pageable);

    Page<ProductDiscount> search(Query query);

    void index(ProductDiscount entity);
}

class ProductDiscountSearchRepositoryInternalImpl implements ProductDiscountSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final ProductDiscountRepository repository;

    ProductDiscountSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, ProductDiscountRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<ProductDiscount> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<ProductDiscount> search(Query query) {
        SearchHits<ProductDiscount> searchHits = elasticsearchTemplate.search(query, ProductDiscount.class);
        List<ProductDiscount> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(ProductDiscount entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
