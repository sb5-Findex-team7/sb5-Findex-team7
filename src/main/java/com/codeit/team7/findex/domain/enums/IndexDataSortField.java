package com.codeit.team7.findex.domain.enums;

import lombok.Getter;

@Getter
public enum IndexDataSortField {
  baseDate("baseDate"),
  marketPrice("marketPrice"),
  closingPrice("closingPrice"),
  highPrice("highPrice"),
  lowPrice("lowPrice"),
  versus("versus"),
  fluctuationRate("fluctuationRate"),
  tradingQuantity("tradingQuantity"),
  tradingPrice("tradingPrice"),
  marketTotalAmount("marketTotalAmount");

  private final String column;
  IndexDataSortField(String column) { this.column = column; }
  public String column() { return column; }
}
