package com.okestro.assignment.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class OpenSearchResponseDto {

    private int took;
    private boolean timed_out;
    @JsonAlias("_shards")
    private Shards shards;
    private Hits hits;
    private Aggregations aggregations;


    @Getter
    public static class Shards {
        private int total;
        private int successful;
        private int skipped;
        private int failed;
    }

    @Getter
    public static class Hits {
        private Total total;
        private Object max_score;
        private List<Object> hits;
    }

    @Getter
    public static class Total {
        private int value;
        @JsonAlias("relation")
        private String relation;
    }

    @Getter
    public static class Aggregations {
        @JsonAlias("sterms#main_aggregation")
        private MainAggregation mainAggregation;
    }

    @Getter
    public static class MainAggregation {
        @JsonAlias("doc_count_error_upper_bound")
        private int docCountErrorUpperBound;
        @JsonAlias("sum_other_doc_count")
        private int sumOtherDocCount;
        private List<Bucket> buckets;
    }


    @Getter
    public static class Bucket {
        private String key;
        @JsonAlias("doc_count")
        private int docCount;
        @JsonAlias("date_histogram#date_histogram")
        private DateHistogram dateHistogram;
    }

    @Getter
    public static class DateHistogram {
        private List<DateBucket> buckets;
    }

    @Getter
    public static class DateBucket {
        @JsonAlias("key_as_string")
        private String keyAsString;
        private long key;
        @JsonAlias("doc_count")
        private int docCount;
        @JsonAlias("vm_hypervisor_status_cpu_utillization")
        private Value vmHypervisorStatusCpuUtilization;
    }

    @Getter
    public static class Value {
        private double value;
    }
}
