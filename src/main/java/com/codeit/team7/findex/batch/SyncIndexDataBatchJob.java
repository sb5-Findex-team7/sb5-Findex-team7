package com.codeit.team7.findex.batch;

import com.codeit.team7.findex.service.SyncIndexDataService;
import com.codeit.team7.findex.service.SyncIndexInfoService;
import com.codeit.team7.findex.util.StockDateUtil;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SyncIndexDataBatchJob {

  private final StockDateUtil stockDateUtil;
  private final SyncIndexInfoService syncIndexInfoService;
  private final SyncIndexDataService syncIndexDataService;

  private static final Logger logger = LoggerFactory.getLogger(SyncIndexDataBatchJob.class);

  @Scheduled(fixedRate = 60 * 60 * 1000) // 1시간 마다 실행
  public void runBatchJob() {
    LocalDate latestDate = stockDateUtil.getLatestDate();

    logger.info("batch Job 실행 {}", Instant.now());

    try {
      syncIndexInfoService.sync(latestDate);
      syncIndexDataService.sync(latestDate);

    } catch (Exception e) {
      logger.error("batch Job 중 에러 발생");
      logger.error(Arrays.toString(e.getStackTrace()));
      logger.error(e.getMessage());
    }

  }


}
