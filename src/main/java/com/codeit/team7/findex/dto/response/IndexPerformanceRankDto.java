package com.codeit.team7.findex.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class IndexPerformanceRankDto {

  IndexPerformanceDto performance;
  @Setter
  Integer rank;


  @Getter
  @Builder
  @Schema(name = "IndexPerformanceDto")
  public static class IndexPerformanceDto {

    @Schema(description = "지수 정보 ID", example = "1")
    private Long indexInfoId;

    @Schema(description = "지수 분류", example = "주식")
    private String indexClassification;

    @Schema(description = "지수 이름", example = "KOSPI")
    private String indexName;

    @Schema(description = "대비", example = "12.5")
    private BigDecimal versus;

    @Schema(description = "등락률", example = "0.85")
    private BigDecimal fluctuationRate;

    @Schema(description = "현재가", example = "1234.56")
    private BigDecimal currentPrice;

    @Schema(description = "이전가", example = "1222.06")
    private BigDecimal beforePrice;
  }
}
