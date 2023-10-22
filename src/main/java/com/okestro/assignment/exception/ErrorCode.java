package com.okestro.assignment.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
    INVALID_INPUT_VALUE(400,  "입력값이 올바르지 않습니다."),
    INVALID_HOSTID_VALUE(400,  "HOST ID를 다시 입력해주세요."),
    INVALID_INTERVAL_VALUE(400,  "분 단위인 [ 1 ~ 60 ]분 사이에 숫자로 입력해주세요."),
    INVALID_FROM_VALUE(400,  "시간 단위인 [ 1 ~ 24 ]시간 사이에 숫자로 입력해주세요."),
    INVALID_SIZE_VALUE(400,  "TOP [ 1 ~ 10 ] 사이에 숫자로 입력해주세요."),
    INVALID_SORT_VALUE(400,  "[ asc , desc ] 으로 입력해주세요."),
    INVALID_RESOURCETYPE_VALUE(400,  "자원 타입 [ vsphere, vcenter, cluster, host, vm ] 입력해주세요."),
    INVALID_OBJECTID_VALUE(400,  "OBJECT ID를 입력해주세요."),

    NOT_FOUND_VALUE(404, "데이터가 없습니다"),

    INTERNAL_SERVER_ERROR(500,  "INTERNAL SERVER ERROR"),



    BUCKET_NOT_FOUND(400,  "버킷명이 존재하지 않습니다.");

    private final String message;
    private final int status;

    ErrorCode(final int status, final String message) {
        this.status = status;
        this.message = message;
    }

}