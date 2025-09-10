package com.codeit.team7.findex.mapper.syncjob;

import com.codeit.team7.findex.domain.entity.IndexInfo;
import com.codeit.team7.findex.dto.PatchSyncConfigCommand;
import com.codeit.team7.findex.dto.SyncConfigDto;
import com.codeit.team7.findex.dto.response.SyncConfigResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SyncConfigMapper {

  @Mapping(target = "indexInfoId", source = "id")
  SyncConfigDto toDto(IndexInfo entity);

  SyncConfigResponse toResponse(SyncConfigDto dto);

  @Mapping(target = "indexInfoId", source = "indexInfoId")
  @Mapping(target = "enabled", source = "enabled")
  PatchSyncConfigCommand toCommand(Long indexInfoId, Boolean enabled);
}
