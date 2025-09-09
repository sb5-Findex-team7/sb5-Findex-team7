package com.codeit.team7.findex.service.impl;

import static com.codeit.team7.findex.domain.enums.JobType.INDEX_INFO;
import static java.time.format.DateTimeFormatter.BASIC_ISO_DATE;

import com.codeit.team7.findex.domain.entity.SyncJob;
import com.codeit.team7.findex.dto.GetNewIndexInfosResult;
import com.codeit.team7.findex.dto.request.StockMarketIndexRequest;
import com.codeit.team7.findex.dto.response.StockMarketIndexResponse;
import com.codeit.team7.findex.dto.response.StockMarketIndexResponse.Item;
import com.codeit.team7.findex.repository.SyncJobRepository;
import com.codeit.team7.findex.service.OpenApiService;
import com.codeit.team7.findex.util.OpenApiUtil;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OpenApiServiceImpl implements OpenApiService {

  private final OpenApiUtil openApiUtil;
  private final SyncJobRepository syncJobRepository;
  private static final int MAX_ITEMS = 200;


  @Override
  @Transactional(readOnly = true)
  public GetNewIndexInfosResult GetNewIndexInfos() {

    LocalDate today = LocalDate.now();
    LocalDate yesterday = LocalDate.now().minusDays(1);
    // 오늘 시작 Instant (00:00:00)
    Instant startOfToday = today.atStartOfDay(ZoneId.systemDefault()).toInstant();
    // 내일 시작 Instant (오늘 끝 경계)
    Instant startOfTomorrow = today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

    // 어제 시작 / 오늘 시작
    Instant startOfYesterday = yesterday.atStartOfDay(ZoneId.systemDefault()).toInstant();
    Instant startOfToday2 = today.atStartOfDay(ZoneId.systemDefault()).toInstant();

    SyncJob indexInfoJobToday = syncJobRepository
        .findFirstByJobTimeBetweenAndJobTypeAndWorkerNot
            (startOfToday, startOfTomorrow, INDEX_INFO.name(), "system")
        .orElse(null);

    SyncJob indexInfoJobYesterday = syncJobRepository
        .findFirstByJobTimeBetweenAndJobTypeAndWorkerNot
            (startOfYesterday, startOfToday2, INDEX_INFO.name(), "system")
        .orElse(null);

    // return 1 이미 Today 데이터가 존재하는 경우
    if (indexInfoJobToday != null) {
      return GetNewIndexInfosResult.builder()
          .isToUpdate(false)
          .BaseDate(today)
          .items(List.of())
          .build();
    } // return 2 이미 Yesterday 데이터가 존재하는 경우  (Today 데이터 가져와야함)
    else if (indexInfoJobYesterday != null) {

      List<Item> items = getNewIndexInfosByBaseDate(today);
      if (items == null || items.isEmpty()) {
        return GetNewIndexInfosResult.builder()
            .isToUpdate(false)
            .BaseDate(yesterday)
            .items(List.of())
            .build();
      }
      return GetNewIndexInfosResult.builder()
          .isToUpdate(true)
          .BaseDate(today)
          .items(items)
          .build();
    } // return 3 yesterday 데이터도 없는 경우 (yesterday 데이터부터 가져와야 함)
    else {
      List<Item> items = getNewIndexInfosByBaseDate(yesterday);
      return GetNewIndexInfosResult.builder()
          .isToUpdate(true)
          .BaseDate(yesterday)
          .items(items)
          .build();
    }
  }

  private List<Item> getNewIndexInfosByBaseDate(LocalDate baseDate) {
    StockMarketIndexResponse response = openApiUtil.fetchStockMarketIndex(
        StockMarketIndexRequest.builder()
            .basDt(baseDate.format(BASIC_ISO_DATE))
            .numOfRows(MAX_ITEMS)
            .build());
    if (response != null
        && response.getResponse() != null
        && response.getResponse().getBody() != null
        && response.getResponse().getBody().getItems() != null
    ) {
      return response.getResponse().getBody().getItems().getItem();
    } else {
      throw new RuntimeException("OpenAPI Response value is NULL");
    }
  }

}
