package com.codeit.team7.findex.controller;

import com.codeit.team7.findex.domain.enums.SortedDirection;
import com.codeit.team7.findex.domain.enums.SyncConfigSoredField;
import com.codeit.team7.findex.dto.CursorPageResponseSyncConfigDto;
import com.codeit.team7.findex.dto.command.GetSyncConfigCommand;
import com.codeit.team7.findex.dto.request.PatchSyncRequest;
import com.codeit.team7.findex.dto.response.CursorPageResponseSyncConfigResponse;
import com.codeit.team7.findex.dto.response.SyncConfigResponse;
import com.codeit.team7.findex.mapper.syncjob.SyncConfigMapper;
import com.codeit.team7.findex.service.SyncConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auto-sync-configs")
public class SyncConfigController {

  private final SyncConfigService syncConfigService;
  private final SyncConfigMapper syncConfigMapper;

  @PatchMapping("/{id}")
  public ResponseEntity<SyncConfigResponse> patchSyncConfigs(
      @PathVariable Long id,
      @RequestBody @Valid PatchSyncRequest request
  ) {

    return ResponseEntity.ok(syncConfigMapper.toResponse(
            syncConfigService.patchSyncConfig(
                syncConfigMapper.toCommand(id, request.getEnabled())
            )
        )
    );
  }

  // size controller null 처리 해주기
  @GetMapping
  public ResponseEntity<CursorPageResponseSyncConfigResponse> getSyncConfigs(
      @RequestParam(required = false) Long indexInfoId,
      @RequestParam(required = false) Boolean enabled,
      @RequestParam(required = false) Long idAfter,
      @RequestParam(required = false) String cursor,
      @RequestParam(required = false) SyncConfigSoredField sortField,
      @RequestParam(required = false) SortedDirection sortDirection,
      @RequestParam(required = false, defaultValue = "10") Integer size

  ) {
    CursorPageResponseSyncConfigDto dto = syncConfigService.getSyncJobs(
        GetSyncConfigCommand.builder()
            .indexInfoId(indexInfoId)
            .enabled(enabled)
            .idAfter(idAfter)
            .cursor(cursor)
            .sortField(sortField)
            .sortDirection(sortDirection)
            .size(size <= 0 ? 10 : size)
            .build());

    return ResponseEntity.ok(syncConfigMapper.toCursorPageResponse(dto));
  }

}
