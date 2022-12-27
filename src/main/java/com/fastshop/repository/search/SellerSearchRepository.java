package com.fastshop.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.fastshop.domain.Seller;
import com.fastshop.repository.SellerRepository;
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
 * Spring Data Elasticsearch repository for the {@link Seller} entity.
 */
public interface SellerSearchRepository extends ElasticsearchRepository<Seller, Long>, SellerSearchRepositoryInternal {}

interface SellerSearchRepositoryInternal {
    Page<Seller> search(String query, Pageable pageable);

    Page<Seller> search(Query query);

    void index(Seller entity);
}

class SellerSearchRepositoryInternalImpl implements SellerSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final SellerRepository repository;

    SellerSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, SellerRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Seller> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<Seller> search(Query query) {
        SearchHits<Seller> searchHits = elasticsearchTemplate.search(query, Seller.class);
        List<Seller> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Seller entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
