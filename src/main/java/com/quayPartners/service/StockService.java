package com.quayPartners.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quayPartners.criteria.FilterCriteria;
import com.quayPartners.dto.StockInfo;
import com.quayPartners.repository.StockInfoJpa;
import com.quayPartners.repository.StockInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
@Service
public class StockService {

    @Value("${nasdaq.apiKey}")
    private final String apiKey = null;
    @Value("${nasdaq.url}")
    private final String url = null;

    @Autowired
    private StockInfoRepository stockInfoRepository;

    public ResponseEntity<StockInfo> getStockData(FilterCriteria criteria) throws IOException, InterruptedException, URISyntaxException {
        HttpResponse<String> response = null;
        ObjectMapper objectMapper = new ObjectMapper();

        var stockInfoJpa = stockInfoRepository.findStockInfo(criteria.getStartDate(), criteria.getEndDate(), criteria.getStockTinkerName(), criteria.getCollapse());
        if (stockInfoJpa != null) {
            return ResponseEntity.status(HttpStatusCode.valueOf(stockInfoJpa.getStatusCode())).body(objectMapper.readValue(stockInfoJpa.getResponse(), StockInfo.class));
        } else {
            response = HttpClient.newHttpClient().send(
                    HttpRequest.newBuilder()
                            .uri(new URI(buildUriComponent(criteria).toUriString()))
                            .GET()
                            .build(), HttpResponse.BodyHandlers.ofString());
            stockInfoRepository.save(new StockInfoJpa(criteria.getStockTinkerName(), criteria.getStartDate(), criteria.getEndDate(), criteria.getCollapse(), response.statusCode(), response.body()));
            return ResponseEntity.status(HttpStatusCode.valueOf(response.statusCode())).body(objectMapper.readValue(response.body(), StockInfo.class));
        }
    }

    private UriComponents buildUriComponent(FilterCriteria criteria) {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(url)
                .path("{stockTickerName}.json")
                .queryParams(makeFilterMap(criteria))
                .queryParam("api_key", apiKey)
                .buildAndExpand(criteria.getStockTinkerName());
    }

    private MultiValueMap<String, String> makeFilterMap(FilterCriteria criteria) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        MultiValueMap<String, String> filterMap = new LinkedMultiValueMap<>();
        filterMap.add("start_date", formatter.format(criteria.getStartDate()));
        filterMap.add("end_date", formatter.format(criteria.getEndDate()));
        filterMap.add("collapse", criteria.getCollapse());
        return filterMap;

    }
}
