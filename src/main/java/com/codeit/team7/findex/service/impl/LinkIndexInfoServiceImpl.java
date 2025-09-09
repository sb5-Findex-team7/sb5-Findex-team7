package com.codeit.team7.findex.service.impl;

import com.codeit.team7.findex.domain.entity.IndexInfo;
import com.codeit.team7.findex.dto.SyncJobDto;
import com.codeit.team7.findex.dto.response.StockMarketIndexResponse.Item;
import com.codeit.team7.findex.repository.IndexInfoRepository;
import com.codeit.team7.findex.repository.SyncJobRepository;
import com.codeit.team7.findex.service.LinkIndexInfoService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkIndexInfoServiceImpl implements LinkIndexInfoService {

  private final IndexInfoRepository indexInfoRepository;
  private final SyncJobRepository syncJobRepository;


  @Override
  public List<SyncJobDto> LinkIndexInfos(List<Item> newIndexInfos) {
    List<String> indexClassifications = newIndexInfos.stream().map(Item::getIndexClassification)
        .toList();
    // 1. newIndexInfos 에서 이미 있는 엔티티 업데이트
    List<IndexInfo> IndexInfosToUpdate = indexInfoRepository.findAllByIndexClassificationIn(
        indexClassifications);

    // 1-2. indexClassification To IndexInfo Map 만들기 새로운 데이터라면 value -> null
    Map<String, IndexInfo> indexInfoMap = IndexInfosToUpdate.stream()
        .collect(Collectors.toMap(IndexInfo::getIndexClassification, info -> info));

    // 2. newIndexInfos 에서 없는 엔티티 새로 생성

    return List.of();
  }

  // 1. IndexInfo를 새로운 데이터와 비교해 변화가 있다면 인스턴스 update, 없다면 그대로
  private void updateIndexInfo(IndexInfo indexInfo, Item newItem) {

    if (!indexInfo.getIndexName().equals(newItem.getIndexName())) {
      indexInfo.setIndexName(newItem.getIndexName());
    }
    if (!indexInfo.getItemCount().equals(newItem.getItemsCount())) {
      indexInfo.setItemCount(newItem.getItemsCount());
    }
    if (!indexInfo.getBasePointInTime().equals(newItem.getBasePointInDate())) {
      indexInfo.setBasePointInTime(newItem.getBasePointInDate());
    }
    if (!indexInfo.getBaseIndex().equals(newItem.getBaseIndex().intValue())) {
      indexInfo.setBaseIndex(newItem.getBaseIndex().intValue());
    }
  }
}
