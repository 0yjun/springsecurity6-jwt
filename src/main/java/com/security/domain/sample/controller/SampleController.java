package com.security.domain.sample.controller;

import com.security.domain.sample.model.response.SampleResponse;
import com.security.domain.sample.service.SampleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="Sample API", description = "SAMPLE API")
@RestController
@RequestMapping("/api/sample")
@Slf4j
@RequiredArgsConstructor
public class SampleController {
    private final SampleService sampleService;

    @Operation(
            summary = "SAMPLE SEACH",
            description = "sample 조회 기능 정의"
    )
    @GetMapping("/search/{name}")
    public SampleResponse searchSample(
            @PathVariable("name") String name
    ){
        return sampleService.findByName(name);
    }
}
