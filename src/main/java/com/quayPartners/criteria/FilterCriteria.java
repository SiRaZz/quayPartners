package com.quayPartners.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
public class FilterCriteria {
    private String stockTinkerName;
    private Date startDate;
    private Date endDate;
    private String collapse;
    private int daySize;
}
