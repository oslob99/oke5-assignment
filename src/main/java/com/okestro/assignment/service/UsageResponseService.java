package com.okestro.assignment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.okestro.assignment.dto.UsageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.opensearch.action.search.SearchResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsageResponseService {

    private final ObjectMapper mapper;

    public UsageResponseDTO processUsageResponse(SearchResponse response) {
        try {
            JsonNode root = mapper.readTree(response.toString());

            JsonNode aggregations = root.get("aggregations");
            JsonNode stermsHostId = aggregations.get("sterms#hostId");
            JsonNode buckets = stermsHostId.get("buckets");
            String hostId = buckets.get(0).get("key").asText();

            List<UsageResponseDTO.ValueTimeDTO> valueTimeList = new ArrayList<>();

            for (JsonNode dateBucket : buckets.get(0).get("date_histogram#interval_cpu_usage").get("buckets")) {
                String timestamp = dateBucket.get("key_as_string").asText();
                double value = dateBucket.get("max#max_cpu_usage").get("value").asDouble();

                valueTimeList.add(UsageResponseDTO.ValueTimeDTO.builder()
                        .Timestamp(timestamp)
                        .Value(value)
                        .build());
            }

            return UsageResponseDTO.builder()
                    .hostId(hostId)
                    .result(valueTimeList)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
