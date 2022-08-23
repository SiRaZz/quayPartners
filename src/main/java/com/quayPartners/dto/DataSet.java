package com.quayPartners.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "dataset_code",
        "database_code",
        "name",
        "description",
        "refreshed_at",
        "newest_available_date",
        "oldest_available_date",
        "column_names",
        "frequency",
        "type",
        "premium",
        "limit",
        "transform",
        "column_index",
        "start_date",
        "end_date",
        "data",
        "collapse",
        "order",
        "database_id"
})
public class DataSet {

    @JsonProperty("id")
    private long id;
    @JsonProperty("dataset_code")
    private String datasetCode;
    @JsonProperty("database_code")
    private String databaseCode;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("refreshed_at")
    private String refreshedAt;
    @JsonProperty("newest_available_date")
    private String newestAvailableDate;
    @JsonProperty("oldest_available_date")
    private String oldestAvailableDate;
    @JsonProperty("column_names")
    private List<String> columnNames = null;
    @JsonProperty("frequency")
    private String frequency;
    @JsonProperty("type")
    private String type;
    @JsonProperty("premium")
    private boolean premium;
    @JsonProperty("limit")
    private Object limit;
    @JsonProperty("transform")
    private Object transform;
    @JsonProperty("column_index")
    private Object columnIndex;
    @JsonProperty("start_date")
    private String startDate;
    @JsonProperty("end_date")
    private String endDate;
    @JsonProperty("data")
    private List<List<String>> data = null;
    @JsonProperty("collapse")
    private String collapse;
    @JsonProperty("order")
    private Object order;
    @JsonProperty("database_id")
    private long databaseId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();



}
