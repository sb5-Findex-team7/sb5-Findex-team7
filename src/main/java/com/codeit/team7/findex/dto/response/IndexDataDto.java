package com.codeit.team7.findex.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IndexDataDto {

  @Schema(description = "지수 데이터 ID")
  private Long id;

  @Schema(description = "지수 정보 ID")
  private Long indexInfoId;

  @Schema(description = "기준일자")
  private LocalDate baseDate;

  private BigDecimal marketPrice;
  private BigDecimal closingPrice;
  private BigDecimal highPrice;
  private BigDecimal lowPrice;
  private BigDecimal versus;
  private BigDecimal fluctuationRate;
  private Long tradingQuantity;
  private Long tradingPrice;
  private Long marketTotalAmount;

  @Schema(description = "데이터 출처(USER, OPEN_API)")
  private String sourceType;
}
