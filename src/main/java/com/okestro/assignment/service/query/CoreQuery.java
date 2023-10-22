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

        // Query 생성
        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("object_id.keyword", objectId);
        sourceBuilder.query(matchQuery);

        // "_source" 필드 설정
        sourceBuilder.fetchSource(new String[]{"basic.vsphere.cpu.cores","service.adapter","service.type"}, null);

        // 결과 크기 설정
        sourceBuilder.size(1);

        // 실행 순서 설정
        sourceBuilder.sort(SortBuilders.scoreSort());

        // SearchRequest에 SearchSourceBuilder 설정
        searchRequest.source(sourceBuilder);

        return searchRequest;
    }
}
