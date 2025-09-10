package com.codeit.team7.findex.service.impl;

import com.codeit.team7.findex.domain.entity.IndexInfo;
import com.codeit.team7.findex.domain.enums.SyncConfigSoredField;
import com.codeit.team7.findex.dto.CursorPageResponseSyncConfigDto;
import com.codeit.team7.findex.dto.PaginatedResult;
import com.codeit.team7.findex.dto.PatchSyncConfigCommand;
import com.codeit.team7.findex.dto.SyncConfigDto;
import com.codeit.team7.findex.dto.command.GetSyncConfigCommand;
import com.codeit.team7.findex.mapper.syncjob.SyncConfigMapper;
import com.codeit.team7.findex.repository.IndexInfoRepository;
import com.codeit.team7.findex.repository.SyncConfigRepository;
import com.codeit.team7.findex.service.SyncConfigService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SyncConfigServiceImpl implements SyncConfigService {

  private final IndexInfoRepository indexInfoRepository;
  private final SyncConfigRepository syncConfigRepository;
  private final SyncConfigMapper syncConfigMapper;

  @Override
  @Transactional
  public SyncConfigDto patchSyncConfig(PatchSyncConfigCommand command) {
    Long Id = command.getIndexInfoId();
    Boolean enabled = command.getEnabled();

    IndexInfo indexInfo = indexInfoRepository.findById(Id)
        .orElseThrow(() -> new RuntimeException("IndexInfo not found with id: " + Id));

    try {
      if (enabled != null) {
        indexInfo.setEnabled(enabled);
      }
      return syncConfigMapper.toDto(indexInfoRepository.save(indexInfo));
    } catch (Exception e) {
      return syncConfigMapper.toDto(indexInfo);
    }
  }

  @Override
  public CursorPageResponseSyncConfigDto getSyncJobs(GetSyncConfigCommand command) {
    PaginatedResult<IndexInfo> syncConfigs = syncConfigRepository.searchSyncConfig(
        command.getIndexInfoId(),
        command.getEnabled(),
        command.getIdAfter(),
        command.getCursor(),
        command.getSortField(),
        command.getSortDirection(),
        command.getSize()
    );

    List<SyncConfigDto> content = syncConfigs.getContent().stream().map(syncConfigMapper::toDto)
        .toList();

    IndexInfo lastData = syncConfigs.getContent().isEmpty() ? null :
        syncConfigs.getContent().get(syncConfigs.getContent().size() - 1);

    String nextCursor = null;
    Long nextIdAfter = null;
    SyncConfigSoredField sortedField = Optional.ofNullable(command.getSortField())
        .orElse(SyncConfigSoredField.indexName);

    if (lastData != null) {
      if (sortedField == SyncConfigSoredField.indexName) {
        nextCursor = lastData.getIndexName();
      } else { // enabled
        nextCursor = lastData.getEnabled().toString();

      }
      nextIdAfter = lastData.getId();
    }

    return CursorPageResponseSyncConfigDto.builder()
        .content(content)
        .nextCursor(nextCursor)
        .nextIdAfter(nextIdAfter)
        .size(content.size())
        .totalElements(syncConfigs.getTotalElements())
        .hasNext(syncConfigs.getHasNext())
        .build();
  }
}
