package com.okestro.assignment.service.query;

import lombok.RequiredArgsConstructor;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.index.query.BoolQueryBuilder;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.opensearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NetworkQuery {

    public SearchRequest createOpenSearchRequest(int from, String order, int size) {
        SearchRequest searchRequest = new SearchRequest("sym-metric-k8s-node");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // Bool 쿼리 생성
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.filter(QueryBuilders.rangeQuery("@timestamp")
                .from("now-" + from + "h")
                .to("now"));

        sourceBuilder.query(boolQuery);

        // "basic.k8s.node.network.usage.out_bytes" 필드로 정렬
        if (order.equals("asc")){
            sourceBuilder.sort("basic.k8s.node.network.usage.total_bytes", SortOrder.ASC);
        }else {
            sourceBuilder.sort("basic.k8s.node.network.usage.total_bytes", SortOrder.DESC);
        }

        // 결과 크기 설정
        sourceBuilder.size(size);

        // "_source" 필드 설정
        sourceBuilder.fetchSource(new String[] {"basic.k8s.node.network.usage"}, null);

        searchRequest.source(sourceBuilder);

        return searchRequest;
    }

}
