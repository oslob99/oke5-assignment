package com.okestro.assignment.service.query;

import lombok.RequiredArgsConstructor;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.index.query.MatchQueryBuilder;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.opensearch.search.sort.SortBuilders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoreQuery {

    public SearchRequest createOpenSearchRequest(String resourceType, String objectId) {

        SearchRequest searchRequest = new SearchRequest("sym-metric-vmware-"+resourceType);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("object_id.keyword", objectId);
        sourceBuilder.query(matchQuery);

        if (resourceType.equals("vm")) resourceType = "system";

        sourceBuilder.fetchSource(new String[]{"basic." + resourceType + ".cpu.cores","service.adapter","service.type"}, null);

        sourceBuilder.size(1);

        sourceBuilder.sort(SortBuilders.scoreSort());

        searchRequest.source(sourceBuilder);

        return searchRequest;
    }
}
