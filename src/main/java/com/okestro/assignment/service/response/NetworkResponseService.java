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
            JsonNode responseNode = objectMapper.readTree(response.toString());

            JsonNode hitsNode = responseNode.get("hits").get("hits");

            List<NetworkResponseDTO.NetworkUsageData> networkUsageDataList = new ArrayList<>();

            for (JsonNode hitNode : hitsNode) {
                JsonNode sourceNode = hitNode.get("_source");
                JsonNode networkUsageNode = sourceNode.get("basic").get("k8s").get("node").get("network").get("usage");

                NetworkResponseDTO.NetworkUsageData networkUsageData = NetworkResponseDTO.NetworkUsageData.builder()
                        .id(hitNode.get("_id").asText())
                        .inMB(Double.parseDouble(decimalFormat.format(networkUsageNode.get("in_bytes").asDouble() / 1024)))
                        .totalMB(Double.parseDouble(decimalFormat.format(networkUsageNode.get("total_bytes").asDouble() / 1024)))
                        .outMB(Double.parseDouble(decimalFormat.format(networkUsageNode.get("out_bytes").asDouble() / 1024)))
                        .build();

                networkUsageDataList.add(networkUsageData);
            }

            return NetworkResponseDTO.builder()
                    .result(networkUsageDataList)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
