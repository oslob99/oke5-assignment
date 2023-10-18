package com.okestro.assignment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.okestro.assignment.dto.OpenSearchResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.index.query.BoolQueryBuilder;
import org.opensearch.index.query.ExistsQueryBuilder;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.aggregations.AggregationBuilders;
import org.opensearch.search.aggregations.BucketOrder;
import org.opensearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.opensearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.opensearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpenSearchService {

    private final ObjectMapper mapper;
    private final RestHighLevelClient restHighLevelClient;


    public OpenSearchResponseDto getData() {
        try {
            // 검색 요청 생성
            SearchRequest searchRequest = new SearchRequest("sym-metric-vmware-*"); // 색인 이름 지정
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            // 쿼리 구성
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            // Filter 조건 추가
            boolQuery.filter(new ExistsQueryBuilder("basic.host.memory.usage"));
            boolQuery.filter(QueryBuilders.rangeQuery("@timestamp")
                    .from("now-1h")
                    .to("now")
            );
            boolQuery.filter(QueryBuilders.termsQuery("object_id.keyword", "host-57"));

            sourceBuilder.query(boolQuery);
            sourceBuilder.size(0); // 결과 크기

            // Aggregation 설정
            TermsAggregationBuilder termsAgg = AggregationBuilders.terms("main_aggregation")
                    .field("object_id.keyword")
                    .order(BucketOrder.key(true))
                    .size(10000);

            TermsAggregationBuilder termsAggregation = AggregationBuilders.terms("terms_aggregation")
                    .field("object_id.keyword")
                    .size(10000)
                    .order(BucketOrder.key(true));

            TermsAggregationBuilder maxAgg = AggregationBuilders.terms("vm_hypervisor_status_cpu_utillization")
                    .field("basic.host.memory.usage");

            DateHistogramAggregationBuilder dateHistogramAgg = AggregationBuilders.dateHistogram("date_histogram")
                    .field("@timestamp")
                    .fixedInterval(DateHistogramInterval.minutes(20))
                    .order(BucketOrder.key(true));

            maxAgg.subAggregation(dateHistogramAgg);
            termsAggregation.subAggregation(maxAgg);
            termsAgg.subAggregation(termsAggregation);

            sourceBuilder.aggregation(termsAgg);
            searchRequest.source(sourceBuilder);

            // 검색 요청 실행
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            log.info(response.toString());

            // 검색 결과 처리
            // response에서 원하는 데이터를 추출하여 처리할 수 있습니다.
            return mapper.readValue(response.toString(), OpenSearchResponseDto.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                restHighLevelClient.close();
            } catch (IOException e) {
                // 예외 처리
            }
        }
    }

}
