package com.codeit.team7.findex.controller;

import com.codeit.team7.findex.domain.enums.JobStatus;
import com.codeit.team7.findex.domain.enums.JobType;
import com.codeit.team7.findex.domain.enums.SortedDirection;
import com.codeit.team7.findex.domain.enums.SyncJobSortedField;
import com.codeit.team7.findex.dto.CursorPageResponseSyncJobDto;
import com.codeit.team7.findex.dto.GetNewIndexDataResult;
import com.codeit.team7.findex.dto.LinkIndexInfosDto;
import com.codeit.team7.findex.dto.PageResponseSyncJobDto;
import com.codeit.team7.findex.dto.SyncJobDto;
import com.codeit.team7.findex.dto.command.GetSyncJobByOffsetCommand;
import com.codeit.team7.findex.dto.command.GetSyncJobCommand;
import com.codeit.team7.findex.dto.request.LinkIndexDataRequest;
import com.codeit.team7.findex.dto.response.CursorPageResponseSyncJobResponse;
import com.codeit.team7.findex.dto.response.OffsetPageResponseSyncJobResponse;
import com.codeit.team7.findex.dto.response.SyncJobResponse;
import com.codeit.team7.findex.mapper.syncjob.SyncJobMapper;
import com.codeit.team7.findex.service.LinkIndexInfoService;
import com.codeit.team7.findex.service.OpenApiService;
import com.codeit.team7.findex.service.SyncJobService;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/sync-jobs")
public class SyncJobController {

  private final SyncJobService syncJobService;
  private final SyncJobMapper syncJobMapper;
  private final OpenApiService openApiService;
  private final LinkIndexInfoService linkIndexInfoService;

  @GetMapping
  public ResponseEntity<CursorPageResponseSyncJobResponse> getSyncJobs(
      @RequestParam(required = false) JobType jobType,
      @RequestParam(required = false) Long indexInfoId,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate baseDateFrom,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate baseDateTo,
      @RequestParam(required = false) String worker,
      @RequestParam(required = false) LocalDateTime jobTimeFrom,
      @RequestParam(required = false) LocalDateTime jobTimeTo,
      @RequestParam(required = false) JobStatus status, // 작업 상태 (SUCCESS, FAILED)
      @RequestParam(required = false) Long idAfter,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime cursor,
      @RequestParam(required = false) SyncJobSortedField sortField,
      @RequestParam(required = false) SortedDirection sortDirection,
      @RequestParam(required = false, defaultValue = "10") Integer size
  ) {

    CursorPageResponseSyncJobDto syncJobDtos = syncJobService.getSyncJobList(
        GetSyncJobCommand.builder()
            .jobType(jobType)
            .indexInfoId(indexInfoId)
            .baseDateFrom(
                Optional.ofNullable(baseDateFrom)
                    .orElse(null))
            .baseDateTo(Optional.ofNullable(baseDateTo)
                .orElse(null))
            .worker(worker)
            .jobTimeFrom(jobTimeFrom)
            .jobTimeTo(jobTimeTo)
            .status(status)
            .idAfter(idAfter)
            .cursor(cursor)
            .sortField(sortField)
            .sortDirection(sortDirection)
            .size(size <= 0 ? 10 : size)
            .build());

    return ResponseEntity.ok(syncJobMapper.toCursorPageResponse(syncJobDtos));
  }

  @GetMapping("/offset")
  public ResponseEntity<OffsetPageResponseSyncJobResponse> getSyncJobsByOffset(
      @RequestParam(required = false) JobType jobType,
      @RequestParam(required = false) Long indexInfoId,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate baseDateFrom,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate baseDateTo,
      @RequestParam(required = false) String worker,
      @RequestParam(required = false) LocalDateTime jobTimeFrom,
      @RequestParam(required = false) LocalDateTime jobTimeTo,
      @RequestParam(required = false) JobStatus status, // 작업 상태 (SUCCESS, FAILED)
      @RequestParam(required = false) SyncJobSortedField sortField,
      @RequestParam(required = false) SortedDirection sortDirection,
      @RequestParam(required = false, defaultValue = "10") Integer size,
      @RequestParam(required = false, defaultValue = "0") Integer pageNum
  ) {

    PageResponseSyncJobDto syncJobDtos = syncJobService.getSyncJobListByOffset(
        GetSyncJobByOffsetCommand.builder()
            .jobType(jobType)
            .indexInfoId(indexInfoId)
            .baseDateFrom(
                Optional.ofNullable(baseDateFrom)
                    .orElse(null))
            .baseDateTo(Optional.ofNullable(baseDateTo)
                .orElse(null))
            .worker(worker)
            .jobTimeFrom(jobTimeFrom)
            .jobTimeTo(jobTimeTo)
            .status(status)
            .sortField(sortField)
            .sortDirection(sortDirection)
            .size(size <= 0 ? 10 : size)
            .pageNum(pageNum <= 0 ? 0 : pageNum)
            .build());

    return ResponseEntity.ok(syncJobMapper.toOffsetPageResponse(syncJobDtos));
  }

  @PostMapping("/index-infos")
  public ResponseEntity<List<SyncJobResponse>> linkIndexInfos(HttpServletRequest request) {
    String ip = getClientIp(request);

    Instant start = Instant.now();
    List<SyncJobDto> syncJobDtos = linkIndexInfoService.LinkIndexInfos(
        LinkIndexInfosDto.builder()
            .ip(ip)
            .build());
    Instant end = Instant.now();
    Duration totalDuration = Duration.between(start, end);

// 밀리초 단위
    long totalMillis = totalDuration.toMillis();
// 비율 계산
    System.out.println("총 걸린 시간 = " + totalMillis + "ms");
    return ResponseEntity.status(202)
        .body(syncJobDtos.stream()
            .map(syncJobMapper::toResponse)
            .toList());
  }

  @PostMapping("/index-data")
  public ResponseEntity<List<SyncJobResponse>> linkIndexData(HttpServletRequest request,
      @RequestBody LinkIndexDataRequest reqBody) {

    String ip = getClientIp(request);
    // 1. OpenAPI에서 새로운 지수 정보 가져오기
    GetNewIndexDataResult result = openApiService.getNewIndexData(syncJobMapper.toCommand(reqBody));

    // 2. 새로운 지수 정보들을 IndexData 엔티티로 변환 및 저장하고, 각각에 대해 SyncJob 생성
    List<SyncJobDto> syncJobDtos =
        linkIndexInfoService.LinkIndexData(syncJobMapper.toDto(result, ip));

    return ResponseEntity.status(202)
        .body(syncJobDtos.stream()
            .map(syncJobMapper::toResponse)
            .toList());

  }


  private String getClientIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    if (ip != null && !ip.isEmpty()) {
      ip = ip.split(",")[0].trim(); // 프록시를 거쳐온 경우 첫 번째 IP 사용
    } else {
      ip = request.getRemoteAddr(); // 직접 접속한 경우
    }
    return ip;
  }


}
