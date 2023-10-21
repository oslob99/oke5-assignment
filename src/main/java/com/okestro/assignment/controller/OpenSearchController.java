package com.okestro.assignment.controller;

import com.okestro.assignment.dto.UsageResponseDTO;
import com.okestro.assignment.exception.CustomException;
import com.okestro.assignment.exception.ErrorCode;
import com.okestro.assignment.service.OpenSearchService;
import com.okestro.assignment.service.UsageResponseService;
import com.okestro.assignment.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OpenSearchController {

    private final OpenSearchService openSearchService;
    private final ValidationService validationService;


    @GetMapping("/api/vm/usage/{hostId}")
    public Mono<?> getUsage(
            @PathVariable int hostId,
            @RequestParam(required = false, defaultValue = "max") String option,
            @RequestParam(required = false, defaultValue = "30") int interval,
            @RequestParam(required = false, defaultValue = "1") int from
    ){
        log.info("enter!!!! : {} , {} , {} ,{}", hostId, option, interval, from);

        validationService.validateHostId(hostId);
        validationService.validateInterval(interval);
        validationService.validateFrom(from);

//        UsageResponseDTO responseData = openSearchService.getUsageData(hostId, option, interval, from);


        return Mono.just("haha");
//                Mono.just(responseData);
    }

    @GetMapping("/api/k8s/network")
    public Mono<?> getNetwork(
            @RequestParam String option,
            @RequestParam String sort,
            @RequestParam int size
    ){
        log.info("enter!!!! : {} , {} , {}",option, sort, size);


//        UsageResponseDTO responseData = openSearchService.getData(hostId, option, interval, from);

        return Mono.just("haha");
//                Mono.just(responseData);
    }

    @GetMapping("/api/cpu/core")
    public Mono<?> test(
            @RequestParam String resourceType,
            @RequestParam int objectId
    ){
        log.info("enter!!!! : {} , {}", resourceType, objectId);


//        UsageResponseDTO responseData = openSearchService.getData(hostId, option, interval, from);

        return Mono.just("haha");
//                Mono.just(responseData);
    }


}
