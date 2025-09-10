package com.codeit.team7.findex.dto.response;

import com.codeit.team7.findex.domain.enums.PeriodType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IndexChartDto {

  @Schema(description = "지수 정보 ID", example = "1")
  private Long indexInfoId;

  @Schema(description = "지수 분류", example = "주식")
  private String indexClassification;

  @Schema(description = "지수 이름", example = "KOSPI")
  private String indexName;

  @Column(nullable = false, name = "period_type")
  @Enumerated(EnumType.STRING)
  private PeriodType periodType;

  @Schema(description = "일반 데이터 포인트")
  private List<DataPoint> dataPoints;

  @Schema(description = "5일 이동평균 데이터 포인트")
  private List<DataPoint> ma5DataPoints;

  @Schema(description = "20일 이동평균 데이터 포인트")
  private List<DataPoint> ma20DataPoints;

  /**
   * 차트 데이터 포인트 DTO
   */
  @Getter
  @Builder
  public static class DataPoint {

    @Schema(description = "기준일자", example = "2024-01-01")
    private LocalDate date;

    @Schema(description = "종가", example = "1234.56")
    private BigDecimal closingPrice;
  }
}