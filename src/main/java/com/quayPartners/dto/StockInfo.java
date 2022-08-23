package com.quayPartners.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "dataset"
})
public class StockInfo {
    @JsonProperty("dataset")
    private DataSet dataset;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

}
