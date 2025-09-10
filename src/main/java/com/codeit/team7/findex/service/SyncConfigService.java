package com.codeit.team7.findex.service;

import com.codeit.team7.findex.dto.PatchSyncConfigCommand;
import com.codeit.team7.findex.dto.SyncConfigDto;

public interface SyncConfigService {

  SyncConfigDto patchSyncConfig(PatchSyncConfigCommand command);

}
