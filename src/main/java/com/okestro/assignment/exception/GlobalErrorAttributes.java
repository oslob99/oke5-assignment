package com.okestro.assignment.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> map = super.getErrorAttributes(request, options);

        Throwable throwable = getError(request);
        if (throwable instanceof CustomException) {
            CustomException ex = (CustomException) getError(request);
            map.put("message", ex.getErrorCode().getMessage());
            map.put("status", ex.getErrorCode().getStatus());
        }

        Date timestamp = (Date) map.get("timestamp");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedTimestamp = sdf.format(timestamp);
        map.put("timestamp", formattedTimestamp);

// requestId 필드 제거
        map.remove("requestId");


        return map;
    }


}