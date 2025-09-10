package com.codeit.team7.findex.controller;

import com.codeit.team7.findex.dto.request.PatchSyncRequest;
import com.codeit.team7.findex.dto.response.SyncConfigResponse;
import com.codeit.team7.findex.mapper.syncjob.SyncConfigMapper;
import com.codeit.team7.findex.service.SyncConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
      @Valid PatchSyncRequest request
  ) {

    return ResponseEntity.ok(syncConfigMapper.toResponse(
            syncConfigService.patchSyncConfig(
                syncConfigMapper.toCommand(id, request.getEnabled())
            )
        )
    );
  }

}
