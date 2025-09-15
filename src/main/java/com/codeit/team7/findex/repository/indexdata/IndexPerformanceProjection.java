package com.codeit.team7.findex.repository.indexdata;

import java.math.BigDecimal;

public interface IndexPerformanceProjection {
  Long getIndexInfoId();
  String getIndexClassification();
  String getIndexName();
  BigDecimal getVersus();
  BigDecimal getFluctuationRate();
  BigDecimal getCurrentPrice();
  BigDecimal getBeforePrice();
}
