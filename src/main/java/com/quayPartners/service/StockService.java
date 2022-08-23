package com.quayPartners.service;

import com.quayPartners.criteria.FilterCriteria;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.URISyntaxException;


public interface StockService {

    ResponseEntity<?> getStockData(FilterCriteria filterCriteria) throws IOException, InterruptedException, URISyntaxException;
}
