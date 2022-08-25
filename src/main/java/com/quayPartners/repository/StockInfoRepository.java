package com.quayPartners.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface StockInfoRepository extends JpaRepository<StockInfoJpa, Long> {
    StockInfoJpa findStockInfoJpaByStartDateAndEndDateAndStockTinkerNameAndCollapse(Date startDate, Date endDate, String stockTinkerName, String collapse);

}
