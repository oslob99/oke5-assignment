package com.okestro.assignment.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
    INVALID_INPUT_VALUE(400, "ER001", "입력값이 올바르지 않습니다."),
    METHOD_NOT_ALLOWED(405, "ER002", "지원하지 않는 요청 메서드입니다."),
    INTERNAL_SERVER_ERROR(500, "ER004", "INTERNAL SERVER ERROR"),

    HEADER_NOT_FOUND(400, "ER005", "필수 헤더값이 존재하지 않습니다."),
    BUCKET_NOT_FOUND(400, "ER007", "버킷명이 존재하지 않습니다.")
    ;

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

}