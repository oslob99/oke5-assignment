package com.okestro.assignment.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OpenSearchServiceTest {


    @Autowired
    private OpenSearchService openSearchService;

    @Test
    @DisplayName("응답이 제대로 나와야한다")
    void getUsageResponseData(){



//        openSearchService.getUsageData();


    }


}