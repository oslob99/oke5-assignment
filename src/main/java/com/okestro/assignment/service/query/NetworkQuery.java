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

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.filter(QueryBuilders.rangeQuery("@timestamp")
                .from("now-" + from + "h")
                .to("now"));

        sourceBuilder.query(boolQuery);

        if (order.equals("asc")){
            sourceBuilder.sort("basic.k8s.node.network.usage.total_bytes", SortOrder.ASC);
        }else {
            sourceBuilder.sort("basic.k8s.node.network.usage.total_bytes", SortOrder.DESC);
        }

        sourceBuilder.size(size);

        sourceBuilder.fetchSource(new String[] {"basic.k8s.node.network.usage"}, null);

        searchRequest.source(sourceBuilder);

        return searchRequest;
    }

}
