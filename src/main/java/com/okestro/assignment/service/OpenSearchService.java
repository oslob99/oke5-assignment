package com.okestro.assignment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.okestro.assignment.dto.UsageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpenSearchService {

    private final ObjectMapper mapper;
    private final RestHighLevelClient restHighLevelClient;
    private final UsageQuery openSearchQuery;
    private final UsageResponseService usageResponseService;



    public UsageResponseDTO getUsageData(int hostId, String option, int interval, int from) {
        try {
            SearchRequest searchRequest = openSearchQuery.createOpenSearchRequest(hostId, option, interval, from);
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            // ResponseProcessingService를 사용하여 응답 처리
            UsageResponseDTO responseDTO = usageResponseService.processUsageResponse(response);

            String jsonResponse = mapper.writeValueAsString(responseDTO);
            System.out.println(jsonResponse);

            return responseDTO;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
