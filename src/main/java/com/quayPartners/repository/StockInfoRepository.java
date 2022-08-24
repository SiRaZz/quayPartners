package com.quayPartners.repository;

import com.quayPartners.model.StockInfoJpa;
import jakarta.persistence.QueryHint;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface StockInfoRepository extends JpaRepository<StockInfoJpa, Long> {
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    @Query("SELECT s FROM StockInfoJpa s WHERE s.startDate = ?1 and s.endDate = ?2 and  s.stockTinkerName =?3 and s.collapse =?4")
    StockInfoJpa findStockInfo(Date startDate, Date endDate, String stockTinkerName, String collapse);

}
