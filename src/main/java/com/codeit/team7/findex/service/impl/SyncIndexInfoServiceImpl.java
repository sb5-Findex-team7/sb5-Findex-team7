package com.codeit.team7.findex.service.impl;

import static java.time.format.DateTimeFormatter.BASIC_ISO_DATE;

import com.codeit.team7.findex.domain.entity.IndexInfo;
import com.codeit.team7.findex.domain.entity.SyncJob;
import com.codeit.team7.findex.domain.enums.JobType;
import com.codeit.team7.findex.dto.request.StockMarketIndexRequest;
import com.codeit.team7.findex.dto.response.StockMarketIndexResponse;
import com.codeit.team7.findex.dto.response.StockMarketIndexResponse.Item;
import com.codeit.team7.findex.repository.IndexInfoRepository;
import com.codeit.team7.findex.repository.SyncJobRepository;
import com.codeit.team7.findex.service.SyncIndexInfoService;
import com.codeit.team7.findex.util.OpenApiUtil;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SyncIndexInfoServiceImpl implements SyncIndexInfoService {

  private final SyncJobRepository syncJobRepository;
  private final IndexInfoRepository indexInfoRepository;
  private final OpenApiUtil openApiUtil;
  private static final int MAX_ITEMS = 400; //todo 탐구 해보기
  private static final String SYSTEM_WORKER = "system";


  @Override
  @Transactional
  public void sync(LocalDate targetDate) {
    // 1. 최신 데이터를 봤을 때 이미 최신화가 되었는지 확인
    SyncJob syncJob = syncJobRepository.findTopByTargetDateAndWorkerAndJobType(targetDate, "system",
        JobType.INDEX_INFO.name()).orElse(null);

    // 이미 시스템이 동기화를 한 경우
    if (syncJob != null) {
      return;
    }

    // 2. enabled 된 지수 정보(INFO) 최신화
    List<IndexInfo> indexInfo = indexInfoRepository.findAllByEnabled(true);

    // 지수는 classificationName+IndexName 으로 식별이 가능함
    //EX) key -> KRX시리즈+KRX 100 value -> IndextInfo 인스턴스
    Map<String, IndexInfo> targetInfosMap = indexInfo.stream().collect(Collectors.toMap(
        ii -> ii.getIndexName() + ii.getIndexClassification(), Function.identity()
    ));

    List<Item> newData = getNewIndexInfosByBaseDate(targetDate);
    if (newData == null || newData.isEmpty()) {
      return;
    }
    // API 호출 결과 Map
    Map<String, Item> newDataMap = getNewIndexInfosByBaseDate(targetDate)
        .stream().collect(Collectors.toMap(
            item -> item.getIndexName() + item.getIndexClassification(), Function.identity()
        ));

    newDataMap.keySet().forEach(
        k -> {
          boolean exist = targetInfosMap.get(k) != null;

          if (exist) {

            Item newInfo = newDataMap.get(k);
            IndexInfo targetEntity = targetInfosMap.get(k);

            targetEntity.setItemCount(newInfo.getItemsCount());
            targetEntity.setBasePointInTime(newInfo.getBasePointInDate());
            targetEntity.setBaseIndex(newInfo.getBaseIndex());
          }
        }

    );
    List<IndexInfo> updatedEntities = targetInfosMap.values().stream().toList();
    System.out.println(updatedEntities);
    indexInfoRepository.saveAll(updatedEntities);
    // 3. syncJob에 반영
    List<SyncJob> newSyncJobs = updatedEntities.stream().map(ii ->
        SyncJob.builder()
            .indexInfo(ii)
            .jobType(JobType.INDEX_INFO.name())
            .targetDate(targetDate)
            .worker(SYSTEM_WORKER)
            .jobTime(Instant.now())
            .isCompleted(true)
            .build()

    ).toList();
    syncJobRepository.saveAll(newSyncJobs);

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
        && response.getResponse().getBody().getItems().getItem() != null
    ) {

      return response.getResponse().getBody().getItems().getItem();
    } else {
      throw new RuntimeException("OpenAPI Response value is NULL");
    }
  }

}
