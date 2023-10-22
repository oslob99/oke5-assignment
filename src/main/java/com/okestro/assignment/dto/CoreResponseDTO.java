package com.okestro.assignment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CoreResponseDTO {

    private String status;
    private String message;
    private String resourceType;
    private String serviceType;
    private int core;
}
