package com.codeit.team7.findex.dto.response;

import com.codeit.team7.findex.domain.enums.JobType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class SyncJobResponse {

  private final Long id;
  private final JobType jobType;
  private final Long indexInfoId;
  private final LocalDate targetDate;
  private final String worker;
  private final LocalDateTime jobTime;
  private final String result;
}
