package com.okestro.assignment.service.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.okestro.assignment.dto.NetworkResponseDTO;
import lombok.RequiredArgsConstructor;
import org.opensearch.action.search.SearchResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NetworkResponseService {

    private final ObjectMapper objectMapper;

    public NetworkResponseDTO processNetworkResponse(SearchResponse response) {
        try {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            // 오픈서치 응답 JSON 문자열을 JsonNode로 파싱
            JsonNode responseNode = objectMapper.readTree(response.toString());

            // 결과로부터 hits 노드 가져오기
            JsonNode hitsNode = responseNode.get("hits").get("hits");

            // NetworkResponseDTO를 구성할 리스트 초기화
            List<NetworkResponseDTO.NetworkUsageData> networkUsageDataList = new ArrayList<>();

            // 각 hit에서 필요한 데이터 추출
            for (JsonNode hitNode : hitsNode) {
                JsonNode sourceNode = hitNode.get("_source");
                JsonNode networkUsageNode = sourceNode.get("basic").get("k8s").get("node").get("network").get("usage");

                // DTO에 매핑
                NetworkResponseDTO.NetworkUsageData networkUsageData = NetworkResponseDTO.NetworkUsageData.builder()
                        .id(hitNode.get("_id").asText())
                        .inMBytes(Double.parseDouble(decimalFormat.format(networkUsageNode.get("in_bytes").asDouble() / 1024)))
                        .totalMBytes(Double.parseDouble(decimalFormat.format(networkUsageNode.get("total_bytes").asDouble() / 1024)))
                        .outMBytes(Double.parseDouble(decimalFormat.format(networkUsageNode.get("out_bytes").asDouble() / 1024)))
                        .build();

                networkUsageDataList.add(networkUsageData);
            }

            return NetworkResponseDTO.builder()
                    .networkUsageDataList(networkUsageDataList)
                    .build();
        } catch (IOException e) {
            // JSON 파싱 오류 처리
            // 예외를 처리하거나 적절한 에러 응답을 생성하세요.
            e.printStackTrace();
            return null;
        }
    }

}
