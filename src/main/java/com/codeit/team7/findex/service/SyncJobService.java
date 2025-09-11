package com.codeit.team7.findex.service;

import com.codeit.team7.findex.dto.CursorPageResponseSyncJobDto;
import com.codeit.team7.findex.dto.SyncJobDto;
import com.codeit.team7.findex.dto.command.GetSyncJobCommand;
import org.springframework.data.domain.Slice;

public interface SyncJobService {

  CursorPageResponseSyncJobDto getSyncJobList(GetSyncJobCommand getSyncJobCommand);
}

