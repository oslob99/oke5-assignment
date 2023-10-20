package com.okestro.assignment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.okestro.assignment.dto.UsageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpenSearchService {

    private final ObjectMapper mapper;
    private final RestHighLevelClient restHighLevelClient;
    private final UsageQuery openSearchQuery;

    private UsageResponseDTO processOpenSearchResponse(SearchResponse response) throws Exception {
        // JSON 데이터 파싱
        JsonNode root = mapper.readTree(response.toString());

        // "aggregations" 필드 가져오기
        JsonNode aggregations = root.get("aggregations");

        // "sterms#hostId" 필드 가져오기
        JsonNode stermsHostId = aggregations.get("sterms#hostId");

        // "buckets" 배열 가져오기
        JsonNode buckets = stermsHostId.get("buckets");

        // "hostId" 한 번만 출력
        String hostId = buckets.get(0).get("key").asText();

        // "value"와 "timestamp" 정보를 담을 리스트
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
    }


    public UsageResponseDTO getData(int hostId, String option, int interval, int from) {
        try {
            SearchRequest searchRequest = openSearchQuery.createOpenSearchRequest(hostId, option, interval, from);
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            // 응답 처리 메서드 호출
            UsageResponseDTO responseDTO = processOpenSearchResponse(response);

            // DTO 객체를 JSON 문자열로 변환
            String jsonResponse = mapper.writeValueAsString(responseDTO);
            System.out.println(jsonResponse);

            return responseDTO;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
