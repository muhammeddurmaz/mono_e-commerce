package com.fastshop.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.fastshop.domain.SellerStatistics;
import com.fastshop.repository.SellerStatisticsRepository;
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
 * Spring Data Elasticsearch repository for the {@link SellerStatistics} entity.
 */
public interface SellerStatisticsSearchRepository
    extends ElasticsearchRepository<SellerStatistics, Long>, SellerStatisticsSearchRepositoryInternal {}

interface SellerStatisticsSearchRepositoryInternal {
    Page<SellerStatistics> search(String query, Pageable pageable);

    Page<SellerStatistics> search(Query query);

    void index(SellerStatistics entity);
}

class SellerStatisticsSearchRepositoryInternalImpl implements SellerStatisticsSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final SellerStatisticsRepository repository;

    SellerStatisticsSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, SellerStatisticsRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<SellerStatistics> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<SellerStatistics> search(Query query) {
        SearchHits<SellerStatistics> searchHits = elasticsearchTemplate.search(query, SellerStatistics.class);
        List<SellerStatistics> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(SellerStatistics entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
