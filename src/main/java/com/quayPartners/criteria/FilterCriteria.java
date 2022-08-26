package com.quayPartners.criteria;

import lombok.Data;

import java.util.Date;

@Data
public class FilterCriteria {
    private String stockTinkerName;
    private Date startDate;
    private Date endDate;
    private String collapse;
    private int daySize;
}
