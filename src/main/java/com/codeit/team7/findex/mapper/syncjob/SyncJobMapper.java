package com.codeit.team7.findex.mapper.syncjob;

import com.codeit.team7.findex.dto.CursorPageResponseSyncJobDto;
import com.codeit.team7.findex.dto.SyncJobDto;
import com.codeit.team7.findex.dto.command.GetSyncJobCommand;
import com.codeit.team7.findex.dto.request.SyncJobRequest;
import com.codeit.team7.findex.dto.response.CursorPageResponseSyncJobResponse;
import com.codeit.team7.findex.dto.response.SyncJobResponse;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Slice;

@Mapper(componentModel = "spring")
public interface SyncJobMapper {


  GetSyncJobCommand toCommand(SyncJobRequest request);

  SyncJobResponse toResponse(SyncJobDto dto);

  CursorPageResponseSyncJobResponse toCursorPageResponse(CursorPageResponseSyncJobDto dto);

  CursorPageResponseSyncJobDto toCursorPageResponse(Slice<SyncJobDto> slice);
}
