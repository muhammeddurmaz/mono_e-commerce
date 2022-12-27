package com.fastshop.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.fastshop.domain.PropertyDes;
import com.fastshop.repository.PropertyDesRepository;
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
 * Spring Data Elasticsearch repository for the {@link PropertyDes} entity.
 */
public interface PropertyDesSearchRepository extends ElasticsearchRepository<PropertyDes, Long>, PropertyDesSearchRepositoryInternal {}

interface PropertyDesSearchRepositoryInternal {
    Page<PropertyDes> search(String query, Pageable pageable);

    Page<PropertyDes> search(Query query);

    void index(PropertyDes entity);
}

class PropertyDesSearchRepositoryInternalImpl implements PropertyDesSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final PropertyDesRepository repository;

    PropertyDesSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, PropertyDesRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<PropertyDes> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<PropertyDes> search(Query query) {
        SearchHits<PropertyDes> searchHits = elasticsearchTemplate.search(query, PropertyDes.class);
        List<PropertyDes> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(PropertyDes entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
