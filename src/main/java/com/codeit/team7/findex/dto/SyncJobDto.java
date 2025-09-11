package com.codeit.team7.findex.dto;

import com.codeit.team7.findex.domain.entity.SyncJob;
import com.codeit.team7.findex.domain.enums.JobType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class SyncJobDto {

  private final Long id;
  private final JobType jobType;
  private final Long indexInfoId;
  private final LocalDate targetDate;
  private final String worker;
  private final LocalDateTime jobTime;
  private final String result;

  public SyncJobDto(SyncJob syncJob) {
    this.id = syncJob.getId();
    this.jobType = JobType.valueOf(syncJob.getJobType());
    this.indexInfoId = syncJob.getIndexInfo().getId();
    this.targetDate = syncJob.getTargetDate();
    this.worker = syncJob.getWorker();
    this.jobTime = syncJob.getJobTime() != null
        ? LocalDateTime.ofInstant(syncJob.getJobTime(), ZoneId.systemDefault())
        : null;
    this.result = syncJob.getIsCompleted() ? "SUCCESS" : "FAILED";
  }

}
