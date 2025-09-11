package com.codeit.team7.findex.service.impl;

import static java.time.format.DateTimeFormatter.BASIC_ISO_DATE;
import static java.time.format.DateTimeFormatter.ISO_DATE;

import com.codeit.team7.findex.domain.entity.IndexData;
import com.codeit.team7.findex.domain.entity.IndexInfo;
import com.codeit.team7.findex.domain.entity.SyncJob;
import com.codeit.team7.findex.domain.enums.JobType;
import com.codeit.team7.findex.domain.enums.SourceType;
import com.codeit.team7.findex.dto.request.StockMarketIndexRequest;
import com.codeit.team7.findex.dto.response.StockMarketIndexResponse;
import com.codeit.team7.findex.dto.response.StockMarketIndexResponse.Item;
import com.codeit.team7.findex.exception.sync.AlreadyUpdatedException;
import com.codeit.team7.findex.exception.sync.NonNewDataException;
import com.codeit.team7.findex.repository.IndexDataRepository;
import com.codeit.team7.findex.repository.IndexInfoRepository;
import com.codeit.team7.findex.repository.SyncJobRepository;
import com.codeit.team7.findex.service.SyncIndexDataService;
import com.codeit.team7.findex.util.OpenApiUtil;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BatchIndexDataServiceImpl implements SyncIndexDataService {

  private final SyncJobRepository syncJobRepository;
  private final IndexDataRepository indexDataRepository;
  private final IndexInfoRepository indexInfoRepository;

  private final OpenApiUtil openApiUtil;

  private static final int MAX_ITEMS = 400; //todo 탐구 해보기
  private static final String SYSTEM_WORKER = "system";


  @Override
  @Transactional
  public void sync(LocalDate targetDate) {
    // 1. 최신 데이터를 봤을 때 이미 최신화가 되었는지 확인
    SyncJob syncJob = syncJobRepository.findTopByTargetDateAndWorkerAndJobType(
        targetDate,
        "system",
        JobType.INDEX_DATA.name()
    ).orElse(null);

    if (syncJob != null) {
      throw new AlreadyUpdatedException(
          "이미 처리 완료.",
          targetDate.format(ISO_DATE) + "날의 Index Info 최신화가 이미 완료 되었습니다."
      );
    }

    // 2. enabled 된 targetDate 지수 (데이터) 불러오기
    List<IndexInfo> targetInfos = indexInfoRepository.findAllByEnabled(true);
    if (targetInfos.isEmpty()) {
      return;
    }
    // 지수는 classificationName+IndexName 으로 식별이 가능함
    //EX) key -> KRX 시리즈+KRX 100 value -> IndexInfo 인스턴스
    Map<String, IndexInfo> InfoSymbol2IndexInfo = targetInfos
        .stream().collect(Collectors.toMap(this::getIndexSymbol, Function.identity()));

    // API 호출 결과 Map
    Map<String, Item> newDataMap = getNewIndexInfosByBaseDate(targetDate)
        .stream().collect(Collectors.toMap(this::getItemSymbol, Function.identity()));

    List<IndexData> newIndexData = new ArrayList<>();
    List<SyncJob> newSyncJobs = new ArrayList<>();
    newDataMap.keySet().forEach(
        k -> {
          boolean exist = InfoSymbol2IndexInfo.containsKey(k);

          if (exist) {
            Item newInfo = newDataMap.get(k);
            IndexInfo targetIndexInfo = InfoSymbol2IndexInfo.get(k);

            newIndexData.add(
                IndexData.builder()
                    .indexInfo(targetIndexInfo)
                    .baseDate(targetDate)
                    .sourceType(SourceType.OPEN_API.name())
                    .marketPrice(newInfo.getMarketPrice())
                    .closingPrice(newInfo.getClosingPrice())
                    .highPrice(newInfo.getHighPrice())
                    .lowPrice(newInfo.getLowPrice())
                    .versus(newInfo.getVersus())
                    .fluctuationRate(newInfo.getFluctuationRate())
                    .tradingQuantity(newInfo.getTradingQuantity())
                    .tradingPrice(newInfo.getTradingPrice())
                    .marketTotalAmount(newInfo.getMarketTotalAmount())
                    .build()
            );

            newSyncJobs.add(
                SyncJob.builder()
                    .indexInfo(targetIndexInfo)
                    .jobType(JobType.INDEX_DATA.name())
                    .targetDate(targetDate)
                    .worker(SYSTEM_WORKER)
                    .jobTime(Instant.now())
                    .isCompleted(true)
                    .build()
            );
          }
        }

    );

    if (newIndexData.isEmpty()) {
      throw new NonNewDataException(
          "신규 데이터 없음",
          targetDate.format(ISO_DATE) + "날의 신규 데이터가 Open API 에서 조회되지 않았습니다");
    }

    // todo bulkInsert 최적화
    // 1. indexData 삽입
    indexDataRepository.saveAll(newIndexData);

    // 2. syncJob 삽입
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

  private String getIndexSymbol(IndexInfo indexInfo) {
    return indexInfo.getIndexName() + indexInfo.getIndexClassification();
  }

  private String getItemSymbol(Item item) {
    return item.getIndexName() + item.getIndexClassification();
  }

}
