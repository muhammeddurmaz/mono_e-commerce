package com.fastshop.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.fastshop.domain.Property;
import com.fastshop.repository.PropertyRepository;
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
 * Spring Data Elasticsearch repository for the {@link Property} entity.
 */
public interface PropertySearchRepository extends ElasticsearchRepository<Property, Long>, PropertySearchRepositoryInternal {}

interface PropertySearchRepositoryInternal {
    Page<Property> search(String query, Pageable pageable);

    Page<Property> search(Query query);

    void index(Property entity);
}

class PropertySearchRepositoryInternalImpl implements PropertySearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final PropertyRepository repository;

    PropertySearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, PropertyRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Property> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<Property> search(Query query) {
        SearchHits<Property> searchHits = elasticsearchTemplate.search(query, Property.class);
        List<Property> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Property entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
