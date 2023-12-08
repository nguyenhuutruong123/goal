package com.goal.web.rest.controller;

import com.goal.entity.dto.RequestDTO;
import com.goal.service.SearchService;
import com.goal.web.rest.payload.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@RequestMapping("/api")
public class SearchController {
    @Autowired
    private SearchService service;

    @PostMapping("/search")
    public ResponseEntity<RestResponse<Object>> saveGoal(@RequestBody RequestDTO request) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.builder().body(service.getDataIndex(request)).build());
    }
}
