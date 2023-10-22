package com.okestro.assignment.service.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.okestro.assignment.dto.CoreResponseDTO;
import lombok.RequiredArgsConstructor;
import org.opensearch.action.search.SearchResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoreResponseService {

    private final ObjectMapper objectMapper;

    public CoreResponseDTO extractIndexIdCoreFromJsonResponse(SearchResponse response) {
        try {
            // 주어진 JSON 응답 문자열을 JsonNode로 파싱
            JsonNode hitsNode = objectMapper.readTree(response.toString()).get("hits").get("hits").get(0);

            if (hitsNode == null) {
                return CoreResponseDTO.builder()
                        .status("404")
                        .message("FAIL, OBJECT ID를 다시 입력해주세요.")
                        .core(0)
                        .resourceType(null)
                        .serviceType(null)
                        .build();
            }

            // "_source.basic.vsphere.cpu.cores" 값 추출
            int cores = hitsNode.get("_source").get("basic").get("vsphere").get("cpu").get("cores").asInt();

            // "_source.service.adapter" 값 추출
            String resourceType = hitsNode.get("_source").get("service").get("adapter").asText();

            // "_source.service.type" 값 추출
            String serviceType = hitsNode.get("_source").get("service").get("type").asText();

            // 필요한 값을 JSON 형식으로 출력
            return CoreResponseDTO.builder()
                    .status("200")
                    .message("SUCCESS")
                    .core(cores)
                    .resourceType(resourceType)
                    .serviceType(serviceType)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
