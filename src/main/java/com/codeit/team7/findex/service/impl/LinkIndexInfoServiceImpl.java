package com.codeit.team7.findex.service.impl;

import static com.codeit.team7.findex.domain.enums.JobType.INDEX_INFO;
import static com.codeit.team7.findex.domain.enums.SourceType.OPEN_API;

import com.codeit.team7.findex.domain.entity.IndexInfo;
import com.codeit.team7.findex.domain.entity.SyncJob;
import com.codeit.team7.findex.dto.LinkIndexInfosDto;
import com.codeit.team7.findex.dto.SyncJobDto;
import com.codeit.team7.findex.dto.response.StockMarketIndexResponse.Item;
import com.codeit.team7.findex.repository.IndexInfoRepository;
import com.codeit.team7.findex.repository.SyncJobRepository;
import com.codeit.team7.findex.service.LinkIndexInfoService;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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

}
