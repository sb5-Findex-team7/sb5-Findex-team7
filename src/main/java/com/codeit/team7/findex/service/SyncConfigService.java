package com.codeit.team7.findex.service;

import com.codeit.team7.findex.dto.CursorPageResponseSyncConfigDto;
import com.codeit.team7.findex.dto.PatchSyncConfigCommand;
import com.codeit.team7.findex.dto.SyncConfigDto;
import com.codeit.team7.findex.dto.command.GetSyncConfigCommand;

public interface SyncConfigService {

  SyncConfigDto patchSyncConfig(PatchSyncConfigCommand command);

  CursorPageResponseSyncConfigDto getSyncJobs(GetSyncConfigCommand command);
}
