package com.okestro.assignment.service;

import com.okestro.assignment.exception.CustomException;
import com.okestro.assignment.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    public void validateHostId(int hostId) {
        if (hostId < 1) {
            throw new CustomException(ErrorCode.INVALID_HOSTID_VALUE);
        }
    }

    public void validateInterval(int interval) {
        if (interval < 1 || interval > 60) {
            throw new CustomException(ErrorCode.INVALID_INTERVAL_VALUE);
        }
    }

    public void validateFrom(int from) {
        if (from < 1 || from > 24) {
            throw new CustomException(ErrorCode.INVALID_FROM_VALUE);
        }
    }
}