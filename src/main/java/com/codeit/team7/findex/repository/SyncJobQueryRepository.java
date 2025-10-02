package com.codeit.team7.findex.repository;

import com.codeit.team7.findex.domain.entity.SyncJob;
import com.codeit.team7.findex.domain.enums.JobType;
import com.codeit.team7.findex.domain.enums.SortedDirection;
import com.codeit.team7.findex.domain.enums.SyncJobSortedField;
import com.codeit.team7.findex.dto.PaginatedResult;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface SyncJobQueryRepository {

  PaginatedResult<SyncJob> searchSyncJob(
      JobType jobType
      , Long indexInfoId
      , LocalDate baseDateFrom
      , LocalDate baseDateTo
      , String worker
      , LocalDateTime jobTimeFrom
      , LocalDateTime jobTimeTo
      , Boolean status // 작업 상태 (SUCCESS, FAILED)
      , Long idAfter
      , LocalDateTime cursor // 이때 커서는 ID(PK)
      , SyncJobSortedField sortField
      , SortedDirection sortDirection
      , int size);

  PaginatedResult<SyncJob> searchSyncJobByOffset(
      JobType jobType
      , Long indexInfoId
      , LocalDate baseDateFrom
      , LocalDate baseDateTo
      , String worker
      , LocalDateTime jobTimeFrom
      , LocalDateTime jobTimeTo
      , Boolean status // 작업 상태 (SUCCESS, FAILED)
      , SyncJobSortedField sortField
      , SortedDirection sortDirection
      , int size
      , int pageNum

  );

}
