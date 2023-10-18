package com.okestro.assignment.controller;

import com.okestro.assignment.dto.OpenSearchResponseDto;
import com.okestro.assignment.service.OpenSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OpenSearchController {

    private final OpenSearchService openSearchService;


    @GetMapping("/api/test")
    public Mono<OpenSearchResponseDto> test(){
        log.info("enter!!!!");

        OpenSearchResponseDto responseData = openSearchService.getData();

        return Mono.just(responseData);
    }


}
