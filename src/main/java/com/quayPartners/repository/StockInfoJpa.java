package com.quayPartners.repository;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Date;

@Entity
@Table
@Getter
@Setter
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StockInfoJpa {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String stockTinkerName;
    @Column
    private Date startDate;
    @Column
    private Date endDate;
    @Column
    private String collapse;
    @Column
    private int statusCode;
    @Column
    @Lob
    private String  response;


    public StockInfoJpa(String stockTinkerName, Date startDate, Date endDate, String collapse, int statusCode, String response) {
        this.stockTinkerName = stockTinkerName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.collapse = collapse;
        this.statusCode = statusCode;
        this.response = response;
    }

    public StockInfoJpa() {
        super();
    }
}
