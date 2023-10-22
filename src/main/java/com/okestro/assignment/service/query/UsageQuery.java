package com.okestro.assignment.service.query;

import com.okestro.assignment.exception.CustomException;
import com.okestro.assignment.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.index.query.BoolQueryBuilder;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.aggregations.AggregationBuilders;
import org.opensearch.search.aggregations.BucketOrder;
import org.opensearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.opensearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.opensearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.opensearch.search.aggregations.metrics.MaxAggregationBuilder;
import org.opensearch.search.aggregations.metrics.MinAggregationBuilder;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UsageQuery {

    public SearchRequest createOpenSearchRequest(int hostId, String option, int interval, int from) {
        SearchRequest searchRequest = new SearchRequest("sym-metric-vmware-host");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        boolQuery.filter(QueryBuilders.termQuery("object_id.keyword", "host-" + hostId));
        boolQuery.filter(QueryBuilders.rangeQuery("@timestamp").from("now-" + from + "h").to("now"));

        sourceBuilder.query(boolQuery);
        sourceBuilder.size(0);

        TermsAggregationBuilder hostIdAgg = createHostIdAggregation(option , interval);
        sourceBuilder.aggregation(hostIdAgg);
        searchRequest.source(sourceBuilder);

        System.out.println(searchRequest.toString());

        return searchRequest;
    }

    public TermsAggregationBuilder createHostIdAggregation(String option, int interval) {
        TermsAggregationBuilder hostIdAgg = AggregationBuilders.terms("hostId")
                .field("object_id.keyword")
                .size(10000);

        DateHistogramAggregationBuilder dateHistogramAgg = AggregationBuilders.dateHistogram("interval_cpu_usage")
                .field("@timestamp")
                .fixedInterval(DateHistogramInterval.minutes(interval))
                .order(BucketOrder.key(true));

        if ("min".equals(option)) {
            addMinCpuUsageAggregation(dateHistogramAgg);
        } else if ("max".equals(option)) {
            addMaxCpuUsageAggregation(dateHistogramAgg);
        } else {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }
        hostIdAgg.subAggregation(dateHistogramAgg);

        return hostIdAgg;
    }

    private void addMinCpuUsageAggregation(DateHistogramAggregationBuilder dateHistogramAgg) {
        MinAggregationBuilder minCpuUsageAgg = AggregationBuilders.min("min_cpu_usage")
                .field("basic.host.cpu.usage.norm.pct");
        dateHistogramAgg.subAggregation(minCpuUsageAgg);
    }

    private void addMaxCpuUsageAggregation(DateHistogramAggregationBuilder dateHistogramAgg) {
        MaxAggregationBuilder maxCpuUsageAgg = AggregationBuilders.max("max_cpu_usage")
                .field("basic.host.cpu.usage.norm.pct");
        dateHistogramAgg.subAggregation(maxCpuUsageAgg);
    }

}
