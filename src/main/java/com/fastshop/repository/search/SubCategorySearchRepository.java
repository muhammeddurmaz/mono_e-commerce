package com.fastshop.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.fastshop.domain.SubCategory;
import com.fastshop.repository.SubCategoryRepository;
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
 * Spring Data Elasticsearch repository for the {@link SubCategory} entity.
 */
public interface SubCategorySearchRepository extends ElasticsearchRepository<SubCategory, Long>, SubCategorySearchRepositoryInternal {}

interface SubCategorySearchRepositoryInternal {
    Page<SubCategory> search(String query, Pageable pageable);

    Page<SubCategory> search(Query query);

    void index(SubCategory entity);
}

class SubCategorySearchRepositoryInternalImpl implements SubCategorySearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final SubCategoryRepository repository;

    SubCategorySearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, SubCategoryRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<SubCategory> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<SubCategory> search(Query query) {
        SearchHits<SubCategory> searchHits = elasticsearchTemplate.search(query, SubCategory.class);
        List<SubCategory> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(SubCategory entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
