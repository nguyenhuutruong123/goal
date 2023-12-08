package com.goal.service;


import com.goal.entity.dto.RequestDTO;
import org.elasticsearch.action.search.SearchResponse;

import java.io.IOException;

public interface SearchService {
    SearchResponse getDataIndex(RequestDTO request) throws IOException;
}
