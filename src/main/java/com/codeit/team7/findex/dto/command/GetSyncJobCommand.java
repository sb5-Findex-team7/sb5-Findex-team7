package com.codeit.team7.findex.dto.command;

import com.codeit.team7.findex.domain.enums.JobStatus;
import com.codeit.team7.findex.domain.enums.JobType;
import com.codeit.team7.findex.domain.enums.SyncJobSortedField;
import java.time.Instant;
import java.time.LocalDate;

public class GetSyncJobCommand {

  private JobType jobType;
  private Long indexInfoId;
  private LocalDate baseDateFrom;
  private LocalDate baseDateTo;
  private String worker;
  private Instant jobTimeFrom;
  private Instant jobTimeTo;
  private JobStatus status; // 작업 상태 (SUCCESS, FAILED)
  private Long idAfter;
  private Long cursor; // 이때 커서는 ID(PK)
  private SyncJobSortedField sortField;
  // TODO 마무리 해야함

}
