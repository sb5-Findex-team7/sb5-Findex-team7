package com.codeit.team7.findex.service.impl;

import static com.codeit.team7.findex.domain.enums.JobType.INDEX_DATA;
import static com.codeit.team7.findex.domain.enums.JobType.INDEX_INFO;
import static com.codeit.team7.findex.domain.enums.SourceType.OPEN_API;

import com.codeit.team7.findex.domain.entity.IndexData;
import com.codeit.team7.findex.domain.entity.IndexInfo;
import com.codeit.team7.findex.domain.entity.SyncJob;
import com.codeit.team7.findex.dto.LinkIndexInfosDto;
import com.codeit.team7.findex.dto.SyncJobDto;
import com.codeit.team7.findex.dto.response.StockMarketIndexResponse.Item;
import com.codeit.team7.findex.repository.IndexDataRepository;
import com.codeit.team7.findex.repository.IndexInfoRepository;
import com.codeit.team7.findex.repository.SyncJobRepository;
import com.codeit.team7.findex.service.LinkIndexDataDto;
import com.codeit.team7.findex.service.LinkIndexInfoService;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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


  @Override
  @Transactional
  public List<SyncJobDto> LinkIndexInfos(LinkIndexInfosDto dto) {
    List<Item> newIndexInfos = dto.getItems();
    LocalDate baseDate = dto.getBaseDate();
    String workerIp = Optional.ofNullable(dto.getIp()).orElse("unknown");
    if (!newIndexInfos.isEmpty()) {
      // 1. 기존 IndexInfo 데이터 가져오기
      List<IndexInfo> existingIndexInfos = indexInfoRepository.findAllByIndexClassificationIn(
          newIndexInfos.stream()
              .map(Item::getIndexClassification)
              .collect(Collectors.toList())
      );

      // 2. 기존 데이터 맵핑
      Map<String, IndexInfo> existingIndexInfoMap = existingIndexInfos.stream()
          .collect(Collectors.toMap(IndexInfo::getIndexClassification, Function.identity()));

      // 3. 새로운 데이터와 비교하여 업데이트 또는 삽입
      for (Item newItem : newIndexInfos) {
        IndexInfo existingInfo = existingIndexInfoMap.get(newItem.getIndexClassification());
        if (existingInfo != null) {
          // 업데이트 로직 (필요한 필드만 업데이트)
          existingInfo.setIndexName(newItem.getIndexName());
          existingInfo.setItemCount(newItem.getItemsCount());
          existingInfo.setBasePointInTime(newItem.getBasePointInDate());
          existingInfo.setBaseIndex(newItem.getBaseIndex());
        } else {
          // 삽입 로직
          IndexInfo newIndexInfo = IndexInfo.builder()
              .indexClassification(newItem.getIndexClassification())
              .indexName(newItem.getIndexName())
              .itemCount(newItem.getItemsCount())
              .basePointInTime(newItem.getBasePointInDate())
              .baseIndex(newItem.getBaseIndex())
              .sourceType(OPEN_API.name())
              .favorite(false)
              .enabled(false)
              .build();
          existingIndexInfos.add(newIndexInfo);
        }
      }

      // 4. 일괄 저장 todo batch insert 성능 고려
      indexInfoRepository.saveAll(existingIndexInfos);

      // 5. syncJob 생성 및 저장
      List<SyncJob> newSyncJobs = existingIndexInfos.stream().map(ii ->
          SyncJob.builder()
              .indexInfo(ii)
              .jobType(INDEX_INFO.name())
              .targetDate(
                  ii.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDate())
              .worker(workerIp)
              .jobTime(Instant.now())
              .isCompleted(true)
              .build()
      ).toList();

      // todo batch insert 성능 고려
      syncJobRepository.saveAll(newSyncJobs);

      return newSyncJobs.stream().map(SyncJobDto::new).toList();

    } else {
      // 5. BaseDate에서 작업한 Index_info가져오기
      Instant startOfBaseDate = baseDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
      // 내일 시작 Instant (오늘 끝 경계)
      Instant endOfBaseDate = baseDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

      return syncJobRepository.findAllByJobTimeBetweenAndJobType(startOfBaseDate, endOfBaseDate,
          INDEX_INFO.name()).stream().map(SyncJobDto::new).toList();
    }
  }

  @Override
  @Transactional
  public List<SyncJobDto> LinkIndexData(LinkIndexDataDto dto) {
    Map<Long, List<Item>> items = dto.getItems();
    LocalDate baseFromDate = dto.getBaseFromDate();
    LocalDate baseToDate = dto.getBaseToDate();
    String workerIp = Optional.ofNullable(dto.getIp()).orElse("unknown");
    if (!isMapValuesEmpty(items)) {
      // 1. 기존 IndexData IndexInfos 가져오기
      List<Long> infoIds = items.keySet().stream().toList();
      Map<Long, IndexInfo> existingIndexInfos = indexInfoRepository.findAllByIdIn(infoIds)
          .stream()
          .collect(Collectors.toMap(IndexInfo::getId, Function.identity()));
      List<IndexData> existingIndexData = indexDataRepository
          .findAllByIndexInfoIdInAndBaseDateBetween(infoIds, baseFromDate, baseToDate);

      // 2. 새로운 Index Data
      List<IndexData> newIndexDataList = new ArrayList<>();
      List<SyncJob> newSyncJobs = new ArrayList<>();

      infoIds.forEach(infoId ->
          {
            items.get(infoId).forEach(item -> {
              boolean exists = existingIndexData.stream().anyMatch(indexData ->
                  indexData.getIndexInfo().getId().equals(infoId)
                      && indexData.getBaseDate().equals(item.getBaseDate())
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
        .stream().map(SyncJobDto::new)
        .toList();
  }

  private boolean isMapValuesEmpty(Map<Long, List<Item>> map) {
    return map == null || map.values().stream().flatMap(List::stream).toList().isEmpty();
  }

}
