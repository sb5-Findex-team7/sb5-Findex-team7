package com.codeit.team7.findex.batch;

import com.codeit.team7.findex.service.SyncIndexInfoService;
import com.codeit.team7.findex.util.StockDateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SyncIndexDataBatchJob {

  private final StockDateUtil stockDateUtil;
  private final SyncIndexInfoService syncIndexInfoService;

  @Scheduled(fixedRate = 60 * 60 * 1000) // 1시간 마다 실행
  // todo 주식 장이 닫히는 시간 부터 시작
  public void runBatchJob() {

    System.out.println("SyncIndexDataBatchJob 실행됨");
  }


}
