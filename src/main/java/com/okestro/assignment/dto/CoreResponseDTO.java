package com.okestro.assignment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CoreResponseDTO {

    private String index;
    private String objectId;
    private int core;
}
