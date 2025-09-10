package com.codeit.team7.findex.service.impl;

import com.codeit.team7.findex.domain.entity.IndexData;
import com.codeit.team7.findex.domain.entity.IndexInfo;
import com.codeit.team7.findex.domain.enums.PeriodType;
import com.codeit.team7.findex.dto.PaginatedResult;
import com.codeit.team7.findex.dto.command.ExportCsvCommand;
import com.codeit.team7.findex.dto.command.IndexDataQueryCommand;
import com.codeit.team7.findex.dto.response.IndexChartDto;
import com.codeit.team7.findex.dto.response.IndexDataDto;
import com.codeit.team7.findex.dto.response.IndexPerformanceRankDto;
import com.codeit.team7.findex.mapper.IndexDataMapper;
import com.codeit.team7.findex.repository.indexdata.IndexDataRepository;
import com.codeit.team7.findex.service.IndexDataService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IndexDataServiceImpl implements IndexDataService {

  private final IndexDataRepository indexDataRepository;
  private final IndexDataMapper indexDataMapper;

  @Override
  public PaginatedResult<IndexDataDto> getIndexDataList(IndexDataQueryCommand command) {

    LocalDate cursorDate = (command.getCursor() != null && !command.getCursor()
                                                                   .isBlank())
        ? LocalDate.parse(command.getCursor()) : null;
    int pageSizePlusOne = command.getSize() + 1;

    List<IndexData> fetched = indexDataRepository.findByCursorAndFilters(
        command.getIndexInfoId(),
        command.getStartDate(),
        command.getEndDate(),
        cursorDate,
        command.getIdAfter(),
        command.getSortDirection(),
        pageSizePlusOne
    );

    boolean hasNext = fetched.size() > command.getSize();
    List<IndexData> pageItems = hasNext ? fetched.subList(0, command.getSize()) : fetched;

    List<IndexDataDto> content = pageItems.stream()
                                          .map(indexDataMapper::toDto)
                                          .toList();

    String nextCursor = null;
    Long nextIdAfter = null;
    if (hasNext) {
      IndexData last = fetched.get(command.getSize() - 1);
      nextCursor = last.getBaseDate()
                       .toString();
      nextIdAfter = last.getId();
    }

    return PaginatedResult.<IndexDataDto>builder()
                          .content(content)
                          .nextCursor(nextCursor)
                          .nextIdAfter(nextIdAfter)
                          .size(pageItems.size())
                          .hasNext(hasNext)
                          .build();
  }

  @Override
  public List<IndexChartDto> getChartData(Long indexInfoId, PeriodType periodType) {

    List<IndexData> dailyData = indexDataRepository.findByIndexInfoIdOrderByBaseDateAsc(indexInfoId);

    if (dailyData.isEmpty()) return List.of();

    List<IndexData> aggregatedData;
    switch (periodType) {
      case WEEKLY:
        aggregatedData = aggregateWeekly(dailyData);
        break;
      case MONTHLY:
        aggregatedData = aggregateMonthly(dailyData);
        break;
      default:
        aggregatedData = dailyData;
    }

    List<IndexChartDto.DataPoint> dataPoints = aggregatedData.stream()
                                                             .map(d -> IndexChartDto.DataPoint.builder()
                                                                                              .date(d.getBaseDate())
                                                                                              .closingPrice(d.getClosingPrice())
                                                                                              .build())
                                                             .toList();

    List<IndexChartDto.DataPoint> ma5 = calculateMovingAverage(dataPoints, 5);
    List<IndexChartDto.DataPoint> ma20 = calculateMovingAverage(dataPoints, 20);

    IndexInfo info = dailyData.get(0).getIndexInfo();

    return List.of(IndexChartDto.builder()
                                .indexInfoId(info.getId())
                                .indexClassification(info.getIndexClassification())
                                .indexName(info.getIndexName())
                                .periodType(periodType)
                                .dataPoints(dataPoints)
                                .ma5DataPoints(ma5)
                                .ma20DataPoints(ma20)
                                .build());
  }

  private List<IndexData> aggregateWeekly(List<IndexData> dailyData) {
    Map<Integer, IndexData> weekMap = new LinkedHashMap<>();
    for (IndexData d : dailyData) {
      int week = d.getBaseDate().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
      weekMap.put(week, d);
    }
    return new ArrayList<>(weekMap.values());
  }

  private List<IndexData> aggregateMonthly(List<IndexData> dailyData) {
    Map<YearMonth, IndexData> monthMap = new LinkedHashMap<>();
    for (IndexData d : dailyData) {
      YearMonth ym = YearMonth.from(d.getBaseDate());
      monthMap.put(ym, d);
    }
    return new ArrayList<>(monthMap.values());
  }

  private List<IndexChartDto.DataPoint> calculateMovingAverage(List<IndexChartDto.DataPoint> points, int window) {
    if (points.size() < window) return List.of();

    List<IndexChartDto.DataPoint> maPoints = new ArrayList<>();
    for (int i = window - 1; i < points.size(); i++) {
      BigDecimal sum = BigDecimal.ZERO;
      for (int j = i - window + 1; j <= i; j++) {
        sum = sum.add(points.get(j).getClosingPrice());
      }
      BigDecimal avg = sum.divide(BigDecimal.valueOf(window), 2, RoundingMode.HALF_UP);
      maPoints.add(IndexChartDto.DataPoint.builder()
                                          .date(points.get(i).getDate())
                                          .closingPrice(avg)
                                          .build());
    }
    return maPoints;
  }
  @Override
  public List<IndexPerformanceRankDto> getPerformanceRank(PeriodType periodType, int limit) {

    LocalDate endDate = LocalDate.now();
    LocalDate startDate;

    switch (periodType) {
      case WEEKLY -> startDate = endDate.minusDays(6);
      case MONTHLY -> startDate = endDate.minusMonths(1).plusDays(1);
      default -> startDate = endDate;
    }

    List<IndexData> dataInRange = indexDataRepository.findAllByBaseDateBetween(startDate, endDate);

    Map<Long, List<IndexData>> groupedByIndex = dataInRange.stream()
                                                           .collect(Collectors.groupingBy(d -> d.getIndexInfo().getId()));

    List<IndexPerformanceRankDto> rankList = groupedByIndex.values().stream()
                                                           .map(list -> {
                                                             list.sort(Comparator.comparing(IndexData::getBaseDate));
                                                             IndexData first = list.get(0);
                                                             IndexData last = list.get(list.size() - 1);

                                                             BigDecimal versus = last.getClosingPrice().subtract(first.getClosingPrice());
                                                             BigDecimal fluctuationRate = versus
                                                                 .divide(first.getClosingPrice(), 4, RoundingMode.HALF_UP)
                                                                 .multiply(BigDecimal.valueOf(100));

                                                             IndexInfo info = last.getIndexInfo();

                                                             IndexPerformanceRankDto.IndexPerformanceDto performance =
                                                                 IndexPerformanceRankDto.IndexPerformanceDto.builder()
                                                                                                            .indexInfoId(info.getId())
                                                                                                            .indexClassification(info.getIndexClassification())
                                                                                                            .indexName(info.getIndexName())
                                                                                                            .versus(versus)
                                                                                                            .fluctuationRate(fluctuationRate)
                                                                                                            .currentPrice(last.getClosingPrice())
                                                                                                            .beforePrice(first.getClosingPrice())
                                                                                                            .build();

                                                             return IndexPerformanceRankDto.builder()
                                                                                           .performance(performance)
                                                                                           .rank(0) // 나중에 정렬 후 rank 부여
                                                                                           .build();
                                                           })
                                                           .sorted(Comparator.comparing(
                                                               (IndexPerformanceRankDto r) -> r.getPerformance().getFluctuationRate()
                                                           ).reversed())
                                                           .limit(limit)
                                                           .collect(Collectors.toList());

    // 4. 랭크 부여
    for (int i = 0; i < rankList.size(); i++) {
      rankList.get(i).setRank(i + 1);
    }

    return rankList;
  }

  @Override
  public List<IndexPerformanceRankDto.IndexPerformanceDto> getFavoritePerformance(PeriodType periodType) {

    LocalDate endDate = LocalDate.now();
    LocalDate startDate;

    switch (periodType) {
      case WEEKLY -> startDate = endDate.minusDays(6);
      case MONTHLY -> startDate = endDate.minusMonths(1).plusDays(1);
      default -> startDate = endDate;
    }

    List<IndexData> indexDataList = indexDataRepository.findByIndexInfoIsFavoriteAndBaseDateBetween(
        true, startDate, endDate
    );

    Map<Long, List<IndexData>> grouped = indexDataList.stream()
                                                      .collect(Collectors.groupingBy(data -> data.getIndexInfo().getId()));

    List<IndexPerformanceRankDto.IndexPerformanceDto> performances = null;

    return performances;
  }

  private IndexPerformanceRankDto.IndexPerformanceDto calculatePerformance(
      List<IndexData> dataList, IndexInfo info) {

    dataList.sort(Comparator.comparing(IndexData::getBaseDate));
    IndexData first = dataList.get(0);
    IndexData last = dataList.get(dataList.size() - 1);

    BigDecimal versus = last.getClosingPrice().subtract(first.getClosingPrice());
    BigDecimal fluctuationRate = versus
        .divide(first.getClosingPrice(), 4, RoundingMode.HALF_UP)
        .multiply(BigDecimal.valueOf(100));

    return IndexPerformanceRankDto.IndexPerformanceDto.builder()
                                                      .indexInfoId(info.getId())
                                                      .indexClassification(info.getIndexClassification())
                                                      .indexName(info.getIndexName())
                                                      .versus(versus)
                                                      .fluctuationRate(fluctuationRate)
                                                      .currentPrice(last.getClosingPrice())
                                                      .beforePrice(first.getClosingPrice())
                                                      .build();
  }

  @Override
  public ResponseEntity<byte[]> exportCsv(ExportCsvCommand command) {

    List<IndexData> dataList = indexDataRepository.findByFilters(
        command.getIndexInfoId(),
        command.getStartDate(),
        command.getEndDate(),
        command.getSortField(),
        command.getSortDirection()
    );

    StringBuilder sb = new StringBuilder();
    sb.append("기준일자,시가,종가,고가,저가,전일대비,등락률,거래량,거래대금,시가총액\n");

    for (IndexData d : dataList) {
      sb.append(d.getBaseDate())
        .append(",");
      sb.append(d.getMarketPrice())
        .append(",");
      sb.append(d.getClosingPrice())
        .append(",");
      sb.append(d.getHighPrice())
        .append(",");
      sb.append(d.getLowPrice())
        .append(",");
      sb.append(d.getVersus())
        .append(",");
      sb.append(d.getFluctuationRate())
        .append(",");
      sb.append(d.getTradingQuantity())
        .append(",");
      sb.append(d.getTradingPrice())
        .append(",");
      sb.append(d.getMarketTotalAmount())
        .append("\n");
    }

    byte[] bytes = sb.toString()
                     .getBytes(StandardCharsets.UTF_8);

    return ResponseEntity.ok()
                         .header("Content-Disposition", "attachment; filename=\"index_data.csv\"")
                         .header("Content-Type", "text/csv;charset=UTF-8")
                         .body(bytes);
  }

}
