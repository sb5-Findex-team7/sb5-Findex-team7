package com.codeit.team7.findex.dto;

import com.codeit.team7.findex.domain.enums.JobType;
import java.time.Instant;
import java.time.LocalDate;
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
  private final Instant jobTime;
  private final String result;

}
