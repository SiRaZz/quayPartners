package com.quayPartners.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quayPartners.criteria.FilterCriteria;
import com.quayPartners.dto.DataSet;
import com.quayPartners.dto.StockInfo;
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

@Service
public class StockServiceImpl implements StockService {

    @Value("${nasdaq.apiKey}")
    private final String apiKey = null;
    @Value("${nasdaq.url}")
    private final String url = null;

    @Override
    public ResponseEntity<StockInfo> getStockData(FilterCriteria criteria) throws IOException, InterruptedException, URISyntaxException {

        MultiValueMap<String, String> filterMap = new LinkedMultiValueMap<>();
        filterMap.add("start_date", criteria.getStartDate());
        filterMap.add("end_date", criteria.getEndDate());
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
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        StockInfo stockInfo = objectMapper.readValue(response.body(), StockInfo.class);
        return ResponseEntity.status(HttpStatusCode.valueOf(response.statusCode())).body(stockInfo);
    }
}
