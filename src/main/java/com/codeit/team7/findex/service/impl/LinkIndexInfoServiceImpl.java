package com.codeit.team7.findex.service.impl;

import static com.codeit.team7.findex.domain.enums.JobType.INDEX_DATA;
import static com.codeit.team7.findex.domain.enums.JobType.INDEX_INFO;
import static com.codeit.team7.findex.domain.enums.SourceType.OPEN_API;
import static java.time.format.DateTimeFormatter.BASIC_ISO_DATE;

import com.codeit.team7.findex.domain.entity.IndexData;
import com.codeit.team7.findex.domain.entity.IndexInfo;
import com.codeit.team7.findex.domain.entity.SyncJob;
import com.codeit.team7.findex.domain.enums.JobType;
import com.codeit.team7.findex.dto.LinkIndexInfosDto;
import com.codeit.team7.findex.dto.SyncJobDto;
import com.codeit.team7.findex.dto.request.StockMarketIndexRequest;
import com.codeit.team7.findex.dto.response.StockMarketIndexResponse;
import com.codeit.team7.findex.dto.response.StockMarketIndexResponse.Item;
import com.codeit.team7.findex.repository.IndexDataRepository;
import com.codeit.team7.findex.repository.IndexInfoRepository;
import com.codeit.team7.findex.repository.SyncJobRepository;
import com.codeit.team7.findex.service.LinkIndexDataDto;
import com.codeit.team7.findex.service.LinkIndexInfoService;
import com.codeit.team7.findex.util.OpenApiUtil;
import com.codeit.team7.findex.util.StockDateUtil;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LinkIndexInfoServiceImpl implements LinkIndexInfoService {

  private final IndexInfoRepository indexInfoRepository;
  private final IndexDataRepository indexDataRepository;
  private final SyncJobRepository syncJobRepository;

  private final OpenApiUtil openApiUtil;

  private final StockDateUtil stockDateUtil;

  private static final int MAX_ITEMS = 400; //todo 탐구 해보기


  @Override
  @Transactional
  public List<SyncJobDto> LinkIndexInfos(LinkIndexInfosDto dto) {
    final String workerIP = Optional.ofNullable(dto.getIp())
                                    .orElse("unknown" + UUID.randomUUID());

    LocalDate targetDate = stockDateUtil.getLatestDate();
    SyncJob syncJob = syncJobRepository.findTopByTargetDateAndWorkerNotAndJobType(targetDate,
                                           "system",
                                           INDEX_INFO.name())
                                       .orElse(null);

    if (syncJob == null) {
      // 새로운 데이터 insert
      List<IndexInfo> indexInfos = indexInfoRepository.findAll();

      // 지수는 classificationName+IndexName 으로 식별이 가능함
      //EX) key -> KRX 시리즈+KRX 100 value -> IndexInfo 인스턴스
      Map<String, IndexInfo> targetInfosMap = indexInfos.stream()
                                                        .collect(Collectors.toMap(
                                                            ii -> ii.getIndexName()
                                                                  + ii.getIndexClassification(),
                                                            Function.identity()
                                                        ));

      // API 호출 결과 Map
      Map<String, Item> newDataMap = getNewIndexInfosByBaseDate(targetDate)
          .stream()
          .collect(Collectors.toMap(
              item -> item.getIndexName() + item.getIndexClassification(), Function.identity()
          ));

      newDataMap.keySet()
                .forEach(
                    k -> {
                      boolean exist = targetInfosMap.get(k) != null;

                      if (exist) {

                        Item newInfo = newDataMap.get(k);
                        IndexInfo targetEntity = targetInfosMap.get(k);

                        targetEntity.setItemCount(newInfo.getItemsCount());
                        targetEntity.setBasePointInTime(newInfo.getBasePointInDate());
                        targetEntity.setBaseIndex(newInfo.getBaseIndex());
                      } else {
                        Item item = newDataMap.get(k);

                        targetInfosMap.put(k, IndexInfo.builder()
                                                       .indexClassification(
                                                           item.getIndexClassification())
                                                       .indexName(item.getIndexName())
                                                       .itemCount(item.getItemsCount())
                                                       .basePointInTime(item.getBasePointInDate())
                                                       .baseIndex(item.getBaseIndex())
                                                       .sourceType(OPEN_API.name())
                                                       .favorite(false)
                                                       .enabled(false)
                                                       .build());
                      }
                    }
                );

      List<IndexInfo> updatedEntities = targetInfosMap.values()
                                                      .stream()
                                                      .toList();
      indexInfoRepository.saveAll(updatedEntities);

      // 3. syncJob에 반영
      List<SyncJob> newSyncJobs = updatedEntities.stream()
                                                 .map(ii ->
                                                     SyncJob.builder()
                                                            .indexInfo(ii)
                                                            .jobType(JobType.INDEX_INFO.name())
                                                            .targetDate(targetDate)
                                                            .worker(workerIP)
                                                            .jobTime(Instant.now())
                                                            .isCompleted(true)
                                                            .build()
                                                 )
                                                 .toList();
      syncJobRepository.saveAll(newSyncJobs);
//      return newSyncJobs.stream().map(SyncJobDto::new).toList();
    }

    return syncJobRepository.findAllByTargetDateAndWorkerAndJobType
                                (targetDate, workerIP, INDEX_INFO.name())
                            .stream()
                            .map(SyncJobDto::new)
                            .toList();

  }

  @Override
  @Transactional
  public List<SyncJobDto> LinkIndexData(LinkIndexDataDto dto) {
    Map<Long, List<Item>> items = dto.getItems();
    LocalDate baseFromDate = dto.getBaseFromDate();
    LocalDate baseToDate = dto.getBaseToDate();
    String workerIp = Optional.ofNullable(dto.getIp())
                              .orElse("unknown");
    if (!isMapValuesEmpty(items)) {
      // 1. 기존 IndexData IndexInfos 가져오기
      List<Long> infoIds = items.keySet()
                                .stream()
                                .toList();
      Map<Long, IndexInfo> existingIndexInfos = indexInfoRepository.findAllByIdIn(infoIds)
                                                                   .stream()
                                                                   .collect(Collectors.toMap(
                                                                       IndexInfo::getId,
                                                                       Function.identity()));
      List<IndexData> existingIndexData = indexDataRepository
          .findAllByIndexInfoIdInAndBaseDateBetween(infoIds, baseFromDate, baseToDate);

      // 2. 새로운 Index Data
      List<IndexData> newIndexDataList = new ArrayList<>();
      List<SyncJob> newSyncJobs = new ArrayList<>();

      infoIds.forEach(infoId ->
          {
            items.get(infoId)
                 .forEach(item -> {
                   boolean exists = existingIndexData.stream()
                                                     .anyMatch(indexData ->
                                                         indexData.getIndexInfo()
                                                                  .getId()
                                                                  .equals(infoId)
                                                         && indexData.getBaseDate()
                                                                     .equals(item.getBaseDate())
                                                     );
                   if (!exists) {
                     // 2. IndexData 삽입
                     IndexData newIndexData = IndexData.builder()
                                                       .indexInfo(existingIndexInfos.get(infoId))
                                                       .baseDate(item.getBaseDate())
                                                       .sourceType(OPEN_API.name())
                                                       .marketPrice(item.getMarketPrice())
                                                       .closingPrice(item.getClosingPrice())
                                                       .highPrice(item.getHighPrice())
                                                       .lowPrice(item.getLowPrice())
                                                       .versus(item.getVersus())
                                                       .fluctuationRate(item.getFluctuationRate())
                                                       .tradingQuantity(item.getTradingQuantity())
                                                       .tradingPrice(item.getTradingPrice())
                                                       .marketTotalAmount(item.getMarketTotalAmount())
                                                       .build();
                     newIndexDataList.add(newIndexData);

                     // 3. syncJob 생성
                     newSyncJobs.add(SyncJob.builder()
                                            .indexInfo(existingIndexInfos.get(infoId))
                                            .jobType(INDEX_DATA.name())
                                            .targetDate(item.getBaseDate())
                                            .worker(workerIp)
                                            .jobTime(Instant.now())
                                            .isCompleted(true)
                                            .build()
                     );
                   }
                 });
          }
      );

      // 일괄 저장
      indexDataRepository.saveAll(newIndexDataList);
      syncJobRepository.saveAll(newSyncJobs);
    }

    return syncJobRepository.findAllByTargetDateBetweenAndJobType(
                                baseFromDate,
                                baseToDate,
                                INDEX_DATA.name())
                            .stream()
                            .map(SyncJobDto::new)
                            .toList();
  }

  private boolean isMapValuesEmpty(Map<Long, List<Item>> map) {
    return map == null || map.values()
                             .stream()
                             .flatMap(List::stream)
                             .toList()
                             .isEmpty();
  }

  private String getIndexSymbol(IndexInfo indexInfo) {
    return indexInfo.getIndexName() + indexInfo.getIndexClassification();
  }

  private String getItemSymbol(Item item) {
    return item.getIndexName() + item.getIndexClassification();
  }

  private List<Item> getNewIndexInfosByBaseDate(LocalDate baseDate) {
    StockMarketIndexResponse response = openApiUtil.fetchStockMarketIndex(
        StockMarketIndexRequest.builder()
                               .basDt(baseDate.format(BASIC_ISO_DATE))
                               .numOfRows(MAX_ITEMS)
                               .build());
    if (response != null
        && response.getResponse() != null
        && response.getResponse()
                   .getBody() != null
        && response.getResponse()
                   .getBody()
                   .getItems() != null
        && response.getResponse()
                   .getBody()
                   .getItems()
                   .getItem() != null
    ) {

      return response.getResponse()
                     .getBody()
                     .getItems()
                     .getItem();
    } else {
      throw new RuntimeException("OpenAPI Response value is NULL");
    }
  }

}
