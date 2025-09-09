package com.codeit.team7.findex.controller;

import com.codeit.team7.findex.domain.enums.JobStatus;
import com.codeit.team7.findex.domain.enums.JobType;
import com.codeit.team7.findex.domain.enums.SortedDirection;
import com.codeit.team7.findex.domain.enums.SyncJobSortedField;
import com.codeit.team7.findex.dto.CursorPageResponseSyncJobDto;
import com.codeit.team7.findex.dto.command.GetSyncJobCommand;
import com.codeit.team7.findex.dto.response.CursorPageResponseSyncJobResponse;
import com.codeit.team7.findex.mapper.syncjob.SyncJobMapper;
import com.codeit.team7.findex.service.SyncJobService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/sync-jobs")
public class SyncJobController {

  private final SyncJobService syncJobService;
  private final SyncJobMapper syncJobMapper;

  @GetMapping
  public ResponseEntity<CursorPageResponseSyncJobResponse> getSyncJobs(
      @RequestParam(required = false) JobType jobType,
      @RequestParam(required = false) Long indexInfoId,
      @RequestParam(required = false) LocalDate baseDateFrom,
      @RequestParam(required = false) LocalDate baseDateTo,
      @RequestParam(required = false) String worker,
      @RequestParam(required = false) LocalDateTime jobTimeFrom,
      @RequestParam(required = false) LocalDateTime jobTimeTo,
      @RequestParam(required = false) JobStatus status, // 작업 상태 (SUCCESS, FAILED)
      @RequestParam(required = false) Long idAfter,
      @RequestParam(required = false) LocalDateTime cursor, // 이때 커서는 datetime
      @RequestParam(required = false) SyncJobSortedField sortField,
      @RequestParam(required = false) SortedDirection sortDirection,
      @RequestParam(required = false) Integer size
  ) {
    int realSize = Optional.ofNullable(size)
        .filter(s -> s > 0)
        .orElse(10);

    CursorPageResponseSyncJobDto syncJobDtos = syncJobService.getSyncJobList(
        GetSyncJobCommand.builder()
            .jobType(jobType)
            .indexInfoId(indexInfoId)
            .baseDateFrom(baseDateFrom)
            .baseDateTo(baseDateTo)
            .worker(worker)
            .jobTimeFrom(jobTimeFrom)
            .jobTimeTo(jobTimeTo)
            .status(status)
            .idAfter(idAfter)
            .cursor(cursor)
            .sortField(sortField)
            .sortDirection(sortDirection)
            .size(realSize)
            .build());

    return ResponseEntity.ok(syncJobMapper.toCursorPageResponse(syncJobDtos));
  }

}
