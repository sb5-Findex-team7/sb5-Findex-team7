package com.codeit.team7.findex.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IndexDataRequest {

  @Schema(description = "지수 정보 ID", example = "1")
  @NotNull
  private Long indexInfoId;

  @Schema(description = "기준일자 (yyyy-MM-dd)", example = "2024-01-01")
  @NotNull
  private LocalDate baseDate;

  @Schema(description = "시장가", example = "1200.55")
  private BigDecimal marketPrice;

  @Schema(description = "종가", example = "1198.45")
  private BigDecimal closingPrice;

  @Schema(description = "고가", example = "1210.00")
  private BigDecimal highPrice;

  @Schema(description = "저가", example = "1185.25")
  private BigDecimal lowPrice;

  @Schema(description = "대비", example = "-2.10")
  private BigDecimal versus;

  @Schema(description = "등락률", example = "-0.15")
  private BigDecimal fluctuationRate;

  @Schema(description = "거래량", example = "1500000")
  private Long tradingQuantity;

  @Schema(description = "거래대금", example = "1800000000")
  private Long tradingPrice;

  @Schema(description = "시가총액", example = "500000000000")
  private Long marketTotalAmount;
}