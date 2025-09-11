package com.codeit.team7.findex.service.impl;

import com.codeit.team7.findex.domain.entity.IndexData;
import com.codeit.team7.findex.domain.entity.IndexInfo;
import com.codeit.team7.findex.domain.enums.PeriodType;
import com.codeit.team7.findex.domain.enums.SourceType;
import com.codeit.team7.findex.dto.IndexDataScrollRequest;
import com.codeit.team7.findex.dto.PaginatedResult;
import com.codeit.team7.findex.dto.command.ExportCsvCommand;
import com.codeit.team7.findex.dto.command.IndexDataDto;
import com.codeit.team7.findex.dto.command.IndexDataQueryCommand;
import com.codeit.team7.findex.dto.request.IndexDataCreateRequest;
import com.codeit.team7.findex.dto.request.IndexDataUpdateRequest;
import com.codeit.team7.findex.dto.response.CursorPageResponseIndexDataDto;
import com.codeit.team7.findex.dto.response.IndexChartDto;
import com.codeit.team7.findex.dto.response.IndexPerformanceRankDto;
import com.codeit.team7.findex.mapper.IndexDataMapper;
import com.codeit.team7.findex.repository.IndexDataQueryRepository;
import com.codeit.team7.findex.repository.IndexDataRepository;
import com.codeit.team7.findex.service.IndexDataService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IndexDataServiceImpl implements IndexDataService {

  private final IndexDataRepository indexDataRepository;
  private final IndexDataMapper indexDataMapper;
  private final IndexDataQueryRepository indexDataQueryRepository;


  @PersistenceContext
  private EntityManager em;


  @Transactional
  public IndexDataDto create(IndexDataCreateRequest request) {
    if (indexDataRepository.existsByIndexInfoIdAndBaseDate(request.getIndexInfoId(),
        request.getBaseDate())) {
      throw new IllegalArgumentException("이미 존재하는 값입니다.");
    }

    IndexData entity = indexDataMapper.toEntity(request);

    entity.setSourceType(SourceType.USER.name());
    entity.setIndexInfo(em.getReference(IndexInfo.class, request.getIndexInfoId()));

    IndexData saved = indexDataRepository.save(entity);
    return indexDataMapper.toDto(saved);
  }


  @Transactional
  public IndexDataDto update(Long id, IndexDataUpdateRequest request) {
    IndexData indexData = indexDataRepository.findById(id)
                                             .orElseThrow(() -> new NoSuchElementException(
                                                 "아이디를 찾을 수 없습니다." + id));

    if (request.getMarketPrice() != null) {
      indexData.setMarketPrice(request.getMarketPrice());
    }
    if (request.getClosingPrice() != null) {
      indexData.setClosingPrice(request.getClosingPrice());
    }
    if (request.getHighPrice() != null) {
      indexData.setHighPrice(request.getHighPrice());
    }
    if (request.getLowPrice() != null) {
      indexData.setLowPrice(request.getLowPrice());
    }
    if (request.getVersus() != null) {
      indexData.setVersus(request.getVersus());
    }
    if (request.getFluctuationRate() != null) {
      indexData.setFluctuationRate(request.getFluctuationRate());
    }
    if (request.getTradingQuantity() != null) {
      indexData.setTradingQuantity(request.getTradingQuantity());
    }
    if (request.getMarketTotalAmount() != null) {
      indexData.setMarketTotalAmount(request.getMarketTotalAmount());
    }

    IndexData saved = indexDataRepository.save(indexData);
    return indexDataMapper.toDto(saved);
  }

  @Transactional
  public void deleteById(Long id) {
    indexDataRepository.deleteById(id);
  }

  @Transactional(readOnly = true)
  public CursorPageResponseIndexDataDto getIndexData(IndexDataScrollRequest request) {
    LocalDate startDate = request.getStartTime() != null ? request.getStartTime()
                                                                  .toLocalDate() : null;
    LocalDate endDate = request.getEndTime() != null ? request.getEndTime()
                                                              .toLocalDate() : null;
    int size = request.pageSizeOrDefault();

    long totalElements;
    if (request.getIndexInfoId() == null) {
      totalElements = indexDataRepository.count();
    } else {
      totalElements = indexDataQueryRepository.countByIndexInfoId(request.getIndexInfoId());
    }

    List<IndexData> rows = indexDataQueryRepository.fetchPage(
        request.getIndexInfoId(),
        startDate,
        endDate,
        request.getIdAfter(),
        request.sortFieldOrDefault(),
        request.sortDirectionOrDefault(),
        size
    );

    boolean hasNext = rows.size() > size;
    if (hasNext) {
      rows = rows.subList(0, size);
    }

    List<IndexDataDto> content = rows.stream()
                                     .map(indexDataMapper::toDto)
                                     .toList();
    Long nextIdAfter = rows.isEmpty() ? null : rows.get(rows.size() - 1)
                                                   .getId();

    LocalDateTime nextCursor = null;
    if (!rows.isEmpty() && rows.get(rows.size() - 1)
                               .getCreatedAt() != null) {
      nextCursor = LocalDateTime.ofInstant(rows.get(rows.size() - 1)
                                               .getCreatedAt(), ZoneId.systemDefault());
    }

    return CursorPageResponseIndexDataDto.builder()
                                         .content(content)
                                         .nextCursor(nextCursor)
                                         .nextIdAfter(nextIdAfter)
                                         .size(size)
                                         .totalElements(totalElements)
                                         .hasNext(hasNext)
                                         .build();
  }


  @Override
  public PaginatedResult<IndexDataDto> getIndexDataList(
      IndexDataQueryCommand command) {

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
                                          .map(
                                              indexDataMapper::toDto)
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
  public IndexChartDto getChartData(Long indexInfoId, PeriodType periodType) {

    LocalDate endDate = LocalDate.now();
    LocalDate startDate;
    startDate = switch (periodType) {
      case WEEKLY -> endDate.minusWeeks(1);
      case MONTHLY -> endDate.minusMonths(1);
      case QUARTERLY -> endDate.minusMonths(3);
      case YEARLY -> endDate.minusYears(1);
      default -> endDate;
    };

    List<IndexData> data = indexDataRepository.findByIndexInfoIdAndBaseDateBetweenOrderByBaseDateAsc(
        indexInfoId, startDate, endDate);
    if (data.isEmpty()) {

      return IndexChartDto.builder()
                          .indexInfoId(indexInfoId)
                          .indexClassification("")
                          .indexName("")
                          .periodType(periodType)
                          .dataPoints(List.of())
                          .ma5DataPoints(List.of())
                          .ma20DataPoints(List.of())
                          .build();
    }

    List<IndexChartDto.DataPoint> dataPoints = data.stream()
                                                   .map(d -> IndexChartDto.DataPoint.builder()
                                                                                    .date(
                                                                                        d.getBaseDate())
                                                                                    .value(
                                                                                        d.getClosingPrice())
                                                                                    .build())
                                                   .toList();

    List<IndexChartDto.DataPoint> ma5 = calculateMovingAverage(dataPoints, 5);
    List<IndexChartDto.DataPoint> ma20 = calculateMovingAverage(dataPoints, 20);

    IndexInfo info = data.get(0)
                         .getIndexInfo();

    return IndexChartDto.builder()
                        .indexInfoId(info.getId())
                        .indexClassification(info.getIndexClassification())
                        .indexName(info.getIndexName())
                        .periodType(periodType)
                        .dataPoints(dataPoints)
                        .ma5DataPoints(ma5)
                        .ma20DataPoints(ma20)
                        .build();
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
                            .getValue());
      }
      BigDecimal avg = sum.divide(BigDecimal.valueOf(window), 2, RoundingMode.HALF_UP);
      maPoints.add(IndexChartDto.DataPoint.builder()
                                          .date(points.get(i)
                                                      .getDate())
                                          .value(avg)
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
      case MONTHLY -> startDate = endDate.minusMonths(1)
                                         .plusDays(1);
      default -> startDate = endDate;
    }

    List<IndexData> dataInRange = indexDataRepository.findByBaseDateBetween(startDate, endDate);

    Map<Long, List<IndexData>> groupedByIndex = dataInRange.stream()
                                                           .collect(Collectors.groupingBy(
                                                               d -> d.getIndexInfo()
                                                                     .getId()));

    List<IndexPerformanceRankDto> rankList = groupedByIndex.values()
                                                           .stream()
                                                           .map(list -> {
                                                             return IndexPerformanceRankDto.builder()
                                                                                           .performance(
                                                                                               calculatePerformance(
                                                                                                   list))
                                                                                           .rank(
                                                                                               0)
                                                                                           .build();
                                                           })
                                                           .sorted(Comparator.comparing(
                                                                                 (IndexPerformanceRankDto r) -> r.getPerformance()
                                                                                                                 .getFluctuationRate()
                                                                             )
                                                                             .reversed())
                                                           .limit(limit)
                                                           .collect(Collectors.toList());

    for (int i = 0; i < rankList.size(); i++) {
      rankList.get(i)
              .setRank(i + 1);
    }

    // TODO 성능 비교 후 선택
//    List<IndexPerformanceRankDto.IndexPerformanceDto> performanceDtoList = indexDataRepository.findIndexPerformanceBetween(
//                                                                                                  startDate, endDate)
//                                                                                              .stream()
//                                                                                              .map(
//                                                                                                  p -> IndexPerformanceRankDto.IndexPerformanceDto.builder()
//                                                                                                                                                  .indexInfoId(
//                                                                                                                                                      p.getIndexInfoId())
//                                                                                                                                                  .indexClassification(
//                                                                                                                                                      p.getIndexClassification())
//                                                                                                                                                  .indexName(
//                                                                                                                                                      p.getIndexName())
//                                                                                                                                                  .versus(
//                                                                                                                                                      p.getVersus())
//                                                                                                                                                  .fluctuationRate(
//                                                                                                                                                      p.getFluctuationRate())
//                                                                                                                                                  .currentPrice(
//                                                                                                                                                      p.getCurrentPrice())
//                                                                                                                                                  .beforePrice(
//                                                                                                                                                      p.getBeforePrice())
//                                                                                                                                                  .build())
//                                                                                              .sorted(
//                                                                                                  Comparator.comparing(
//                                                                                                                IndexPerformanceRankDto.IndexPerformanceDto::getFluctuationRate
//                                                                                                            )
//                                                                                                            .reversed())
//                                                                                              .limit(
//                                                                                                  limit)
//                                                                                              .toList();
//
//    AtomicInteger counter = new AtomicInteger(1);
//    return performanceDtoList.stream()
//                             .map(p -> {
//                               return IndexPerformanceRankDto.builder()
//                                                             .performance(p)
//                                                             .rank(counter.getAndIncrement())
//                                                             .build();
//                             })
//                             .toList();

    return rankList;
  }

  @Override
  public List<IndexPerformanceRankDto.IndexPerformanceDto> getFavoritePerformance(
      PeriodType periodType) {

    LocalDate endDate = LocalDate.now();
    LocalDate startDate;

    switch (periodType) {
      case WEEKLY -> startDate = endDate.minusDays(6);
      case MONTHLY -> startDate = endDate.minusMonths(1)
                                         .plusDays(1);
      default -> startDate = endDate;
    }

    List<IndexData> indexDataList = indexDataRepository.findByIndexInfoFavoriteAndBaseDateBetween(
        true, startDate, endDate
    );

    Map<Long, List<IndexData>> grouped = indexDataList.stream()
                                                      .collect(Collectors.groupingBy(
                                                          data -> data.getIndexInfo()
                                                                      .getId()));

    // TODO 테스트 후 어느게 빠른지
//    return  indexDataRepository.findIndexPerformanceBetweenWithFavorite(startDate, endDate)
//                               .stream()
//                               .map(p -> IndexPerformanceRankDto.IndexPerformanceDto.builder()
//                                                                                    .indexInfoId(p.getIndexInfoId())
//                                                                                    .indexClassification(p.getIndexClassification())
//                                                                                    .indexName(p.getIndexName())
//                                                                                    .versus(p.getVersus())
//                                                                                    .fluctuationRate(p.getFluctuationRate())
//                                                                                    .currentPrice(p.getCurrentPrice())
//                                                                                    .beforePrice(p.getBeforePrice())
//                                                                                    .build()).toList();

    return grouped.values()
                  .stream()
                  .map(
                      this::calculatePerformance)
                  .toList();
  }

  private IndexPerformanceRankDto.IndexPerformanceDto calculatePerformance(
      List<IndexData> dataList) {

    dataList.sort(
        Comparator.comparing(
            IndexData::getBaseDate));
    IndexData first = dataList.get(
        0);
    IndexData last = dataList.get(
        dataList.size() - 1);

    BigDecimal versus = last.getClosingPrice()
                            .subtract(
                                first.getClosingPrice());
    BigDecimal fluctuationRate = versus
        .divide(
            first.getClosingPrice(),
            4,
            RoundingMode.HALF_UP)
        .multiply(
            BigDecimal.valueOf(
                100));

    IndexInfo info = last.getIndexInfo();

    return IndexPerformanceRankDto.IndexPerformanceDto.builder()
                                                      .indexInfoId(info.getId())
                                                      .indexClassification(
                                                          info.getIndexClassification())
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
