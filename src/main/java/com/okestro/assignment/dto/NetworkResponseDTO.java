package com.okestro.assignment.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class NetworkResponseDTO {

    private List<NetworkUsageData> networkUsageDataList;

    @Getter
    @Builder
    public static class NetworkUsageData {
        private String id;
        private double inBytes;
        private double totalBytes;
        private double outBytes;
    }

}
