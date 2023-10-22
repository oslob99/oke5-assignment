package com.okestro.assignment.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CoreResponseDTO {

    private String status;
    private String message;
    private List<CoreData> result;

    @Getter
    @Builder
    public static class CoreData {
        private String objectId;
        private int core;
        private String resourceType;
        private String serviceType;
    }
}
