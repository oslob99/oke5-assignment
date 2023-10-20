package com.okestro.assignment.controller;

import com.okestro.assignment.dto.UsageResponseDTO;
import com.okestro.assignment.exception.CustomException;
import com.okestro.assignment.exception.ErrorCode;
import com.okestro.assignment.service.OpenSearchService;
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


    @GetMapping("/api/vm/usage/{hostId}")
    public Mono<UsageResponseDTO> test(
            @PathVariable int hostId,
            @RequestParam String option,
            @RequestParam int interval,
            @RequestParam int from,
            BindingResult bindingResult
    ){
        log.info("enter!!!! : {} , {} , {} ,{}", hostId, option, interval, from);

        if (bindingResult.hasErrors()){
            throw new CustomException(ErrorCode.HEADER_NOT_FOUND);
        }

        UsageResponseDTO responseData = openSearchService.getData(hostId, option, interval, from);

        return Mono.just(responseData);
    }


}
