package com.codeit.team7.findex.service.impl;

import static com.codeit.team7.findex.domain.enums.JobStatus.SUCCESS;

import com.codeit.team7.findex.domain.entity.SyncJob;
import com.codeit.team7.findex.domain.enums.SyncJobSortedField;
import com.codeit.team7.findex.dto.CursorPageResponseSyncJobDto;
import com.codeit.team7.findex.dto.PaginatedResult;
import com.codeit.team7.findex.dto.SyncJobDto;
import com.codeit.team7.findex.dto.command.GetSyncJobCommand;
import com.codeit.team7.findex.mapper.syncjob.SyncJobMapper;
import com.codeit.team7.findex.repository.SyncJobRepository;
import com.codeit.team7.findex.service.SyncJobService;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SyncJobServiceImpl implements SyncJobService {

  private final SyncJobRepository syncJobRepository;
  private final SyncJobMapper syncJobMapper;

  @Override
  @Transactional(readOnly = true)
  public CursorPageResponseSyncJobDto getSyncJobList(GetSyncJobCommand getSyncJobCommand) {

    Boolean status = getSyncJobCommand.getStatus() == null ? null :
        getSyncJobCommand.getStatus() == SUCCESS;

    PaginatedResult<SyncJob> syncJobs = syncJobRepository.searchSyncJob(
        getSyncJobCommand.getJobType(),
        getSyncJobCommand.getIndexInfoId(),
        getSyncJobCommand.getBaseDateFrom(),
        getSyncJobCommand.getBaseDateTo(),
        getSyncJobCommand.getWorker(),
        getSyncJobCommand.getJobTimeFrom(),
        getSyncJobCommand.getJobTimeTo(),
        status,
        getSyncJobCommand.getIdAfter(),
        getSyncJobCommand.getCursor(),
        getSyncJobCommand.getSortField(),
        getSyncJobCommand.getSortDirection(),
        getSyncJobCommand.getSize()
    );

    List<SyncJobDto> content = syncJobs.getContent().stream()
        .map(SyncJobDto::new)
        .toList();

    // 마지막 요소 가져오기
    SyncJob lastJob = syncJobs.getContent().isEmpty() ? null :
        syncJobs.getContent().get(syncJobs.getContent().size() - 1);

    LocalDateTime nextCursor = null;
    Long nextIdAfter = null;
    if (lastJob != null) {
      // 정렬 필드에 따라 cursor 설정
      if (getSyncJobCommand.getSortField() == SyncJobSortedField.jobTime) {
        nextCursor = lastJob.getJobTime().atZone(ZoneId.systemDefault()).toLocalDateTime();
      } else if (getSyncJobCommand.getSortField() == SyncJobSortedField.targetDate) {
        // LocalDate → Instant 변환 (예: 자정 기준)
        nextCursor = lastJob.getTargetDate().atStartOfDay(ZoneId.systemDefault()).toLocalDateTime();
      } else {
        nextCursor = lastJob.getJobTime().atZone(ZoneId.systemDefault()).toLocalDateTime();
        ; // 기본값
      }
      nextIdAfter = lastJob.getId();
    }

    return CursorPageResponseSyncJobDto.builder()
        .content(content)
        .nextCursor(nextCursor)
        .nextIdAfter(nextIdAfter)
        .size(content.size())
        .totalElements(syncJobs.getTotalElements()) // Slice는 totalElements 없을 수도 있음, 직접 계산 필요
        .hasNext(syncJobs.getHasNext())
        .build();
  }
}
