package com.okestro.assignment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@Builder
public class UsageResponseDTO {

    private String hostId;
    private List<ValueTimeDTO> result;

    @Getter
    @Builder
    public static class ValueTimeDTO{
        private String Timestamp;
        private double Value;

    }

}
