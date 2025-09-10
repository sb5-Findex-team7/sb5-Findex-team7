package com.codeit.team7.findex.domain.enums;

import lombok.Getter;

@Getter
public enum IndexDataSortField {
  BASE_DATE("baseDate"),
  MARKET_PRICE("marketPrice"),
  CLOSING_PRICE("closingPrice"),
  HIGH_PRICE("highPrice"),
  LOW_PRICE("lowPrice"),
  VERSUS("versus"),
  FLUCTUATION_RATE("fluctuationRate"),
  TRADING_QUANTITY("tradingQuantity"),
  TRADING_PRICE("tradingPrice"),
  MARKET_TOTAL_AMOUNT("marketTotalAmount");

  private final String column;
  IndexDataSortField(String column) { this.column = column; }
  public String column() { return column; }
}
