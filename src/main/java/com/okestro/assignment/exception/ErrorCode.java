package com.okestro.assignment.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
    INVALID_INPUT_VALUE(400,  "입력값이 올바르지 않습니다."),
    INVALID_HOSTID_VALUE(400,  "HOST ID를 다시 입력해주세요."),
    INVALID_INTERVAL_VALUE(400,  "1 ~ 60 사이에 숫자로 입력해주세요."),
    INVALID_FROM_VALUE(400,  "1 ~ 24 사이에 숫자로 입력해주세요."),
    METHOD_NOT_ALLOWED(405,  "지원하지 않는 요청 메서드입니다."),
    INTERNAL_SERVER_ERROR(500,  "INTERNAL SERVER ERROR"),

    HEADER_NOT_FOUND(400,  "필수 헤더값이 존재하지 않습니다."),
    BUCKET_NOT_FOUND(400,  "버킷명이 존재하지 않습니다.");

    private final String message;
    private final int status;

    ErrorCode(final int status, final String message) {
        this.status = status;
        this.message = message;
    }

}