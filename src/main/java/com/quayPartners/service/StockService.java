package com.quayPartners.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quayPartners.criteria.FilterCriteria;
import com.quayPartners.repository.StockInfoJpa;
import com.quayPartners.repository.StockInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ResponseStatusException;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class StockService {

    @Value("${nasdaq.apiKey}")
    private final String apiKey = null;
    @Value("${nasdaq.url}")
    private final String url = null;

    @Autowired
    private StockInfoRepository stockInfoRepository;

    public ResponseEntity<StocksInfo> getStockData(FilterCriteria criteria) {
        HttpResponse<String> response = null;
        ObjectMapper objectMapper = new ObjectMapper();
        StocksInfo stockInfo = new StocksInfo();
        var stockInfoJpa = stockInfoRepository.findStockInfoJpaByStartDateAndEndDateAndStockTinkerNameAndCollapse(criteria.getStartDate(), criteria.getEndDate(), criteria.getStockTinkerName(), criteria.getCollapse());
        try {
            if (stockInfoJpa != null) {
                stockInfo = calculateSimpleMovingAverage(objectMapper.readValue(stockInfoJpa.getResponse(), StocksInfo.class), criteria.getDaySize());
                return ResponseEntity.status(HttpStatusCode.valueOf(stockInfoJpa.getStatusCode())).body(stockInfo);
            } else {
                response = HttpClient.newHttpClient().send(
                        HttpRequest.newBuilder()
                                .uri(new URI(buildUriComponent(criteria).toUriString()))
                                .GET()
                                .build(), HttpResponse.BodyHandlers.ofString());
            }
            stockInfoRepository.save(new StockInfoJpa(criteria.getStockTinkerName(), criteria.getStartDate(), criteria.getEndDate(), criteria.getCollapse(), response.statusCode(), response.body()));
            stockInfo = calculateSimpleMovingAverage(objectMapper.readValue(response.body(), StocksInfo.class), criteria.getDaySize());
            return ResponseEntity.status(HttpStatusCode.valueOf(response.statusCode())).body(stockInfo);
        } catch (URISyntaxException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (IOException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    private UriComponents buildUriComponent(FilterCriteria criteria) {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(url)
                .path("{stockTickerName}.json")
                .queryParams(buildFilterMap(criteria))
                .queryParam("api_key", apiKey)
                .buildAndExpand(criteria.getStockTinkerName());
    }

    private MultiValueMap<String, String> buildFilterMap(FilterCriteria criteria) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        MultiValueMap<String, String> filterMap = new LinkedMultiValueMap<>();
        filterMap.add("start_date", formatter.format(criteria.getStartDate()));
        filterMap.add("end_date", formatter.format(criteria.getEndDate()));
        filterMap.add("collapse", criteria.getCollapse());
        return filterMap;
    }

    private StocksInfo calculateSimpleMovingAverage(StocksInfo info, int daySize) {
        ArrayList<Double> closingPrices = new ArrayList<>();
        ArrayList<Double> simpleMovingAverage = new ArrayList<>();
        if (info.getDataset().getData().size() > daySize) {
            for (List<String> list : info.getDataset().getData()) {
                closingPrices.add(Double.parseDouble(list.get(4)));
            }
            for (int i = 0; i + daySize <= closingPrices.size(); i++) {
                double sum = 0;
                for (int j = i; j < i + daySize; j++) {
                    sum += closingPrices.get(j);
                }
                double average = sum / daySize;
                simpleMovingAverage.add(average);
            }
            info.setSimpleMovingAverageList(simpleMovingAverage);
        } else {
            info.setSimpleMovingAverageMessage("The Simple moving average day must be less than the total number of data");
        }
        return info;
    }
}
