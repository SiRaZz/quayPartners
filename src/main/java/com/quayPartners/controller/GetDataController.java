package com.quayPartners.controller;

import com.quayPartners.criteria.FilterCriteria;
import com.quayPartners.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
public class GetDataController {
    @Autowired
    private StockService stockService;

    @PostMapping(value = "/getData", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getStockInfoByCriteria(@RequestBody FilterCriteria data) throws URISyntaxException, IOException, InterruptedException {
        return stockService.getStockData(data);
    }

}
