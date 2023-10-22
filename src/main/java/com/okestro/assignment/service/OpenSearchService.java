package com.okestro.assignment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.okestro.assignment.dto.CoreResponseDTO;
import com.okestro.assignment.dto.NetworkResponseDTO;
import com.okestro.assignment.dto.UsageResponseDTO;
import com.okestro.assignment.service.query.CoreQuery;
import com.okestro.assignment.service.query.NetworkQuery;
import com.okestro.assignment.service.query.UsageQuery;
import com.okestro.assignment.service.response.CoreResponseService;
import com.okestro.assignment.service.response.NetworkResponseService;
import com.okestro.assignment.service.response.UsageResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpenSearchService {

    private final RestHighLevelClient restHighLevelClient;

    private final UsageQuery openSearchQuery;
    private final UsageResponseService usageResponseService;
    private final NetworkQuery networkQuery;
    private final NetworkResponseService networkResponseService;
    private final CoreQuery coreQuery;
    private final CoreResponseService coreResponseService;


    /**
     * @param hostId : VM 호스트 ID
     * @param option : 최대, 최소
     * @param interval : 시간 간격
     * @param from : 지금으로 부터 몇 시간 전
     * @return : 가공된 데이터를 Response { id , [ value , time ] }
     */
    public UsageResponseDTO getUsageData(int hostId, String option, int interval, int from) {
        try {
            SearchRequest searchRequest = openSearchQuery.createOpenSearchRequest(hostId, option, interval, from);
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            return usageResponseService.processUsageResponse(response);

        }catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 처리 중 오류가 발생했습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("서버 오류입니다.");
        }
    }

    /**
     * @param from : 지금으로 부터 몇 시간 전
     * @param order : 상위, 하위 ( ASC, DESC)
     * @param size : TOP N
     * @return : ID , NETWORK MB USAGE
     */
    public NetworkResponseDTO getNetworkData(int from, String order, int size) {
        try {
            SearchRequest searchRequest = networkQuery.createOpenSearchRequest(from, order, size);
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            return networkResponseService.processNetworkResponse(response);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 처리 중 오류가 발생했습니다.");
        } catch (Exception e) {
            throw new RuntimeException("서버 오류입니다.");
        }
    }

    /**
     * @param resourceType : 자원별 타입
     * @param objectId : 자원별 object ID
     * @return : RESOURCE-TYPE, SERVICE-TYPE, CORE
     */
    public CoreResponseDTO getCoreData(String resourceType, String objectId) {

        try {
            SearchRequest searchRequest = coreQuery.createOpenSearchRequest(resourceType, objectId);
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            return coreResponseService.extractIndexIdCoreFromJsonResponse(response,resourceType,objectId);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 처리 중 오류가 발생했습니다.");
        } catch (Exception e) {
            throw new RuntimeException("서버 오류입니다.");
        }
    }
}
