package com.codeit.team7.findex.mapper.syncjob;

import com.codeit.team7.findex.dto.CursorPageResponseSyncJobDto;
import com.codeit.team7.findex.dto.GetNewIndexDataResult;
import com.codeit.team7.findex.dto.PageResponseSyncJobDto;
import com.codeit.team7.findex.dto.SyncJobDto;
import com.codeit.team7.findex.dto.command.GetNewIndexDataCommand;
import com.codeit.team7.findex.dto.command.GetSyncJobCommand;
import com.codeit.team7.findex.dto.request.LinkIndexDataRequest;
import com.codeit.team7.findex.dto.request.SyncJobRequest;
import com.codeit.team7.findex.dto.response.CursorPageResponseSyncJobResponse;
import com.codeit.team7.findex.dto.response.OffsetPageResponseSyncJobResponse;
import com.codeit.team7.findex.dto.response.SyncJobResponse;
import com.codeit.team7.findex.service.LinkIndexDataDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SyncJobMapper {


  GetSyncJobCommand toCommand(SyncJobRequest request);

  SyncJobResponse toResponse(SyncJobDto dto);

  CursorPageResponseSyncJobResponse toCursorPageResponse(CursorPageResponseSyncJobDto dto);

  OffsetPageResponseSyncJobResponse toOffsetPageResponse(PageResponseSyncJobDto dto);

//  CursorPageResponseSyncJobDto toCursorPageResponse(Slice<SyncJobDto> slice);

  GetNewIndexDataCommand toCommand(LinkIndexDataRequest request);

  @Mapping(target = "ip", source = "ip")
  LinkIndexDataDto toDto(GetNewIndexDataResult result, String ip);
}
