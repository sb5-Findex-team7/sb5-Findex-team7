package com.codeit.team7.findex.service;

import com.codeit.team7.findex.dto.CursorPageResponseSyncJobDto;
import com.codeit.team7.findex.dto.PageResponseSyncJobDto;
import com.codeit.team7.findex.dto.command.GetSyncJobByOffsetCommand;
import com.codeit.team7.findex.dto.command.GetSyncJobCommand;

public interface SyncJobService {

  CursorPageResponseSyncJobDto getSyncJobList(GetSyncJobCommand getSyncJobCommand);

  PageResponseSyncJobDto getSyncJobListByOffset(
      GetSyncJobByOffsetCommand getSyncJobByOffsetCommand);
}

