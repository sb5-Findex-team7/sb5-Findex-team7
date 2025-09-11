package com.codeit.team7.findex.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IndexDataUpdateRequest {
  private BigDecimal marketPrice;
  private BigDecimal closingPrice;
  private BigDecimal highPrice;
  private BigDecimal lowPrice;
  private BigDecimal versus;
  private BigDecimal fluctuationRate;
  private Long tradingQuantity;
  private Long tradingPrice;
  private Long marketTotalAmount;

}
