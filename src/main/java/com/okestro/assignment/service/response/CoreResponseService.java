package com.okestro.assignment.service.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.okestro.assignment.dto.CoreResponseDTO;
import lombok.RequiredArgsConstructor;
import org.opensearch.action.search.SearchResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoreResponseService {

    private final ObjectMapper objectMapper;

    public CoreResponseDTO extractIndexIdCoreFromJsonResponse(SearchResponse response, String resourceType, String objectId) {
        try {
            JsonNode hitsNode = objectMapper.readTree(response.toString()).get("hits").get("hits").get(0);

            if (hitsNode == null) {
                List<CoreResponseDTO.CoreData> coreData = new ArrayList<>();
                coreData.add(CoreResponseDTO.CoreData.builder()
                                .core(0)
                                .objectId(objectId)
                                .resourceType(null)
                                .serviceType(null)
                        .build());
                return CoreResponseDTO.builder()
                        .status("404")
                        .message("FAIL, OBJECT ID를 다시 입력해주세요.")
                        .result(coreData)
                        .build();
            }

            if (resourceType.equals("vm")) resourceType = "system";

            int cores = hitsNode.get("_source").get("basic").get(resourceType).get("cpu").get("cores").asInt();
            String resource = hitsNode.get("_source").get("service").get("adapter").asText();
            String serviceType = hitsNode.get("_source").get("service").get("type").asText();

            List<CoreResponseDTO.CoreData> coreData = new ArrayList<>();
            coreData.add(CoreResponseDTO.CoreData.builder()
                    .core(cores)
                    .objectId(objectId)
                    .resourceType(resource)
                    .serviceType(serviceType)
                    .build());

            return CoreResponseDTO.builder()
                    .status("200")
                    .message("SUCCESS")
                    .result(coreData)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
