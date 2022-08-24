package com.quayPartners.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quayPartners.criteria.FilterCriteria;
import com.quayPartners.dto.DataSet;
import com.quayPartners.dto.StockInfo;
import com.quayPartners.model.StockInfoJpa;
import com.quayPartners.repository.StockInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
import java.util.Date;

@Service
public class StockServiceImpl implements StockService {

    @Value("${nasdaq.apiKey}")
    private final String apiKey = null;
    @Value("${nasdaq.url}")
    private final String url = null;

    @Autowired
    private StockInfoRepository  stockInfoRepository;

    @Override
    public ResponseEntity<StockInfo> getStockData(FilterCriteria criteria) throws IOException, InterruptedException, URISyntaxException {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        HttpResponse<String> response = null;
        StockInfo stockInfo = null;
        ObjectMapper objectMapper = new ObjectMapper();
        MultiValueMap<String, String> filterMap = new LinkedMultiValueMap<>();
        filterMap.add("start_date", formatter.format(criteria.getStartDate()));
        filterMap.add("end_date", formatter.format(criteria.getEndDate()));
        filterMap.add("collapse", criteria.getCollapse());

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(url)
                .path("{stockTickerName}.json")
                .queryParams(filterMap)
                .queryParam("api_key", apiKey)
                .buildAndExpand(criteria.getStockTinkerName());

            HttpRequest request  = HttpRequest.newBuilder()
                    .uri(new URI(uriComponents.toUriString()))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            var stockInfoJpa =  stockInfoRepository.findStockInfo(criteria.getStartDate(), criteria.getEndDate(), criteria.getStockTinkerName(), criteria.getCollapse());
            if(stockInfoJpa != null) {
                stockInfo = objectMapper.readValue(stockInfoJpa.getResponse(), StockInfo.class);
                return ResponseEntity.status(HttpStatusCode.valueOf(stockInfoJpa.getStatusCode())).body(stockInfo);
            } else {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
                stockInfoRepository.save(new StockInfoJpa(criteria.getStockTinkerName(), criteria.getStartDate(), criteria.getEndDate(), criteria.getCollapse(), response.statusCode(), response.body()));
                stockInfo = objectMapper.readValue(response.body(), StockInfo.class);
                return ResponseEntity.status(HttpStatusCode.valueOf(response.statusCode())).body(stockInfo);
            }
    }
}
