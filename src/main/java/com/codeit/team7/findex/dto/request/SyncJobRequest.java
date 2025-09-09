package com.codeit.team7.findex.dto.request;

import com.codeit.team7.findex.domain.enums.JobStatus;
import com.codeit.team7.findex.domain.enums.JobType;
import com.codeit.team7.findex.domain.enums.SortedDirection;
import com.codeit.team7.findex.domain.enums.SyncJobSortedField;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SyncJobRequest {

  private final JobType jobType;
  private final Long indexInfoId;
  private final LocalDate baseDateFrom;
  private final LocalDate baseDateTo;
  private final String worker;
  private final LocalDateTime jobTimeFrom;
  private final LocalDateTime jobTimeTo;
  private final JobStatus status; // 작업 상태 (SUCCESS, FAILED)
  private final Long idAfter;
  private final LocalDateTime cursor; // 이때 커서는 datetime
  private final SyncJobSortedField sortField;
  private final SortedDirection sortDirection;
  private final int size;

}
