package com.codeit.team7.findex;

import com.codeit.team7.findex.domain.entity.IndexInfo;
import com.codeit.team7.findex.domain.entity.SyncJob;
import com.codeit.team7.findex.domain.enums.JobType;
import com.codeit.team7.findex.repository.IndexInfoRepository;
import com.codeit.team7.findex.repository.SyncJobRepository;
import com.codeit.team7.findex.service.SyncIndexInfoService;
import com.codeit.team7.findex.util.OpenApiUtil;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class FindexApplicationTests {

  @Autowired
  OpenApiUtil openApiUtil;

  @Autowired
  SyncIndexInfoService syncIndexInfoService;

  @Autowired
  IndexInfoRepository indexInfoRepository;
  @Autowired
  SyncJobRepository syncJobRepository;

  @Autowired
  private EntityManager em;

  @Test
  @Transactional
  @Rollback(value = false)
  void contextLoads() throws InterruptedException {

    Instant startTime = Instant.now();
    LocalDate targetDate = LocalDate.now().minusDays(1);

    // 실행
    syncIndexInfoService.sync(targetDate);
    em.flush();

    // DB에 들어갔는지 확인 (트랜잭션 안이므로 보임)
    List<SyncJob> job = syncJobRepository
        .findAllByTargetDateAndWorkerAndJobType(targetDate, "system", JobType.INDEX_INFO.name());

    List<IndexInfo> indexInfos = indexInfoRepository.findAll().stream()
        .filter(ii -> ii.getCreatedAt().isAfter(startTime)
            || ii.getUpdatedAt() != null && ii.getUpdatedAt().isAfter(startTime)).toList();
    job.forEach(System.out::println);
    indexInfos.forEach(System.out::println);

    System.out.println("저장된 SyncJob = " + job);

    System.out.println("jobSize" + job.size());
    System.out.println("indexInfos" + indexInfos.size());

  }

}
