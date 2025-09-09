package com.codeit.team7.findex.service.impl;

import com.codeit.team7.findex.domain.entity.IndexData;
import com.codeit.team7.findex.domain.entity.IndexInfo;
import com.codeit.team7.findex.dto.response.IndexChartDto;
import com.codeit.team7.findex.dto.response.IndexPerformanceRankDto;
import com.codeit.team7.findex.repository.IndexDataRepository;
import com.codeit.team7.findex.service.IndexDataService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IndexDataServiceImpl implements IndexDataService {

  private final IndexDataRepository indexDataRepository;

  @Override
  public List<IndexChartDto> getChartData(Long indexInfoId, String periodType) {

    List<IndexData> dataList = indexDataRepository.findByIndexInfoIdOrderByBaseDateAsc(indexInfoId);

    List<IndexChartDto.DataPoint> dataPoints = dataList.stream()
                                                       .map(d -> IndexChartDto.DataPoint.builder()
                                                                                        .date(
                                                                                            d.getBaseDate())
                                                                                        .closingPrice(
                                                                                            d.getClosingPrice())
                                                                                        .build())
                                                       .toList();

    // 이동평균 계산
    List<IndexChartDto.DataPoint> ma5 = calculateMovingAverage(dataPoints, 5);
    List<IndexChartDto.DataPoint> ma20 = calculateMovingAverage(dataPoints, 20);

    IndexData first = dataList.get(0);
    IndexInfo info = first.getIndexInfo();

    return List.of(
        IndexChartDto.builder()
                     .indexInfoId(info.getId())
                     .indexClassification(info.getIndexClassification())
                     .indexName(info.getIndexName())
                     .periodType(periodType)
                     .dataPoints(dataPoints)
                     .ma5DataPoints(ma5)
                     .ma20DataPoints(ma20)
                     .build()
    );
  }

  private List<IndexChartDto.DataPoint> calculateMovingAverage(List<IndexChartDto.DataPoint> points,
      int window) {

    if (points.size() < window) {
      return List.of();
    }

    List<IndexChartDto.DataPoint> maPoints = new ArrayList<>();
    for (int i = window - 1; i < points.size(); i++) {
      BigDecimal sum = BigDecimal.ZERO;
      for (int j = i - window + 1; j <= i; j++) {
        sum = sum.add(points.get(j)
                            .getClosingPrice());
      }
      BigDecimal avg = sum.divide(BigDecimal.valueOf(window), 2, RoundingMode.HALF_UP);
      maPoints.add(IndexChartDto.DataPoint.builder()
                                          .date(points.get(i)
                                                      .getDate())
                                          .closingPrice(avg)
                                          .build());
    }
    return maPoints;
  }

  @Override
  public List<IndexPerformanceRankDto> getPerformanceRank(String periodType, int limit) {

    Pageable pageable = PageRequest.of(0, limit); // 상위 limit 개만 조회
    List<IndexData> latestData = indexDataRepository.findLatestDataByPeriodType(periodType,
        pageable);

    List<IndexPerformanceRankDto> rankList = new ArrayList<>();
    int rank = 1;
    for (IndexData d : latestData) {
      IndexInfo info = d.getIndexInfo();
      IndexPerformanceRankDto.IndexPerformanceDto performance = IndexPerformanceRankDto.IndexPerformanceDto.builder()
                                                                                                           .indexInfoId(
                                                                                                               info.getId())
                                                                                                           .indexClassification(
                                                                                                               info.getIndexClassification())
                                                                                                           .indexName(
                                                                                                               info.getIndexName())
                                                                                                           .versus(
                                                                                                               d.getVersus())
                                                                                                           .fluctuationRate(
                                                                                                               d.getFluctuationRate())
                                                                                                           .currentPrice(
                                                                                                               d.getClosingPrice())
                                                                                                           .beforePrice(
                                                                                                               d.getClosingPrice()
                                                                                                                .subtract(
                                                                                                                    d.getVersus()))
                                                                                                           .build();

      rankList.add(IndexPerformanceRankDto.builder()
                                          .performance(performance)
                                          .rank(rank++)
                                          .build());
    }
    return rankList;
  }

  @Override
  public List<IndexPerformanceRankDto.IndexPerformanceDto> getFavoritePerformance(
      String periodType) {

    List<IndexData> favoriteData = indexDataRepository.findFavoriteData(periodType);

    return favoriteData.stream()
                       .map(d -> {
                         IndexInfo info = d.getIndexInfo();
                         return IndexPerformanceRankDto.IndexPerformanceDto.builder()
                                                                           .indexInfoId(
                                                                               info.getId())
                                                                           .indexClassification(
                                                                               info.getIndexClassification())
                                                                           .indexName(
                                                                               info.getIndexName())
                                                                           .versus(d.getVersus())
                                                                           .fluctuationRate(
                                                                               d.getFluctuationRate())
                                                                           .currentPrice(
                                                                               d.getClosingPrice())
                                                                           .beforePrice(
                                                                               d.getClosingPrice()
                                                                                .subtract(
                                                                                    d.getVersus()))
                                                                           .build();
                       })
                       .toList();
  }

  @Override
  public ResponseEntity<byte[]> exportCsv(Long indexInfoId, String startDate, String endDate,
      String sortField, String sortDirection) {
    return null;
  }
}
