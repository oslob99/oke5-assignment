package com.okestro.assignment.service.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.okestro.assignment.dto.UsageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.action.search.SearchResponse;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsageResponseService {

    private final ObjectMapper objectMapper;

    public UsageResponseDTO processUsageResponse(SearchResponse response) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            DecimalFormat decimalFormat = new DecimalFormat("#.##");

            JsonNode root = objectMapper.readTree(response.toString());
            JsonNode aggregations = root.get("aggregations");
            JsonNode stermsHostId = aggregations.get("sterms#hostId");
            JsonNode buckets = stermsHostId.get("buckets");

            if (buckets != null && buckets.isArray() && !buckets.isEmpty()) {
                String hostId = buckets.get(0).get("key").asText();

                List<UsageResponseDTO.ValueTimeDTO> valueTimeList = new ArrayList<>();
                for (JsonNode dateBucket : buckets.get(0).get("date_histogram#interval_cpu_usage").get("buckets")) {
                    String timestampStr = dateBucket.get("key_as_string").asText();
                    Instant timestamp = Instant.parse(timestampStr);
                    double value = dateBucket.get("max#max_cpu_usage").get("value").asDouble();

                    valueTimeList.add(UsageResponseDTO.ValueTimeDTO.builder()
                            .timestamp(timestamp.atZone(ZoneId.systemDefault()).format(formatter))
                            .Value(Double.parseDouble(decimalFormat.format(value)))
                            .build());
                }

                return UsageResponseDTO.builder()
                        .status("200")
                        .message("SUCCESS")
                        .hostId(hostId)
                        .result(valueTimeList)
                        .build();
            } else {
                return UsageResponseDTO.builder()
                        .status("404")
                        .message("FAIL, 데이터가 없습니다.")
                        .build();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
