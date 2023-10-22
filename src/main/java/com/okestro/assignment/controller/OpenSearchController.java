package com.okestro.assignment.controller;

import com.okestro.assignment.dto.NetworkResponseDTO;
import com.okestro.assignment.dto.UsageResponseDTO;
import com.okestro.assignment.exception.CustomException;
import com.okestro.assignment.exception.ErrorCode;
import com.okestro.assignment.service.OpenSearchService;
import com.okestro.assignment.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
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


    /**
     *
     * @param hostId : VM 호스트 ID
     * @param option : 최대, 최소
     * @param interval : 시간 간격
     * @param from : 지금으로 부터 몇 시간 전
     * @return : 가공된 데이터를 Response { id , [ value , time ] }
     */
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

        UsageResponseDTO responseData = openSearchService.getUsageData(hostId, option, interval, from);

        return Mono.just(responseData);
    }

    /**
     *
     * @param from : 지금으로 부터 몇 시간 전
     * @param order : 상위, 하위 ( ASC, DESC)
     * @param size : TOP N
     * @return : ID , NETWORK MB USAGE
     */
    @GetMapping("/api/k8s/network")
    public Mono<?> getNetwork(
            @RequestParam(required = false, defaultValue = "1") int from,
            @RequestParam(required = false, defaultValue = "desc") String order,
            @RequestParam(required = false, defaultValue = "5") int size
    ){
        log.info("enter!!!! : {} , {} , {}",from, order, size);

        validationService.validateFrom(from);
        validationService.validateSort(order);
        validationService.validateSize(size);

        NetworkResponseDTO networkData = openSearchService.getNetworkData(from, order, size);

        return Mono.just(networkData);
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
