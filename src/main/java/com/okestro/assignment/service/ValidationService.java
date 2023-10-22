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

    public void validateSize(int size) {
        if (size < 1 || size > 10) {
            throw new CustomException(ErrorCode.INVALID_SIZE_VALUE);
        }
    }

    public void validateSort(String sort) {
        if (!"asc".equalsIgnoreCase(sort) && !"desc".equalsIgnoreCase(sort)) {
            throw new CustomException(ErrorCode.INVALID_SORT_VALUE);
        }
    }

    private boolean isValidResourceType(String resourceType) {
        String[] validTypes = {"vsphere", "vcenter", "cluster", "host", "vm"};
        for (String validType : validTypes) {
            if (validType.equals(resourceType)) {
                return true;
            }
        }
        return false;
    }

    public void validateResourceType(String resourceType){
        if (!isValidResourceType(resourceType)){
            throw new CustomException(ErrorCode.INVALID_RESOURCETYPE_VALUE);
        }
    }

    public void validateObjectId(String objectId){
        if (objectId.isEmpty()){
            throw new CustomException(ErrorCode.INVALID_OBJECTID_VALUE);
        }
    }

}