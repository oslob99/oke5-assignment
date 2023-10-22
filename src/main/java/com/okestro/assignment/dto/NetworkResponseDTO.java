package com.okestro.assignment.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class NetworkResponseDTO {

    private List<NetworkUsageData> result;

    @Getter
    @Builder
    public static class NetworkUsageData {
        private String id;
        private double inMB;
        private double totalMB;
        private double outMB;
    }

}
