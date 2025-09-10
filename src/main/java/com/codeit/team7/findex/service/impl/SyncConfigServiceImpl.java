package com.codeit.team7.findex.service.impl;

import com.codeit.team7.findex.domain.entity.IndexInfo;
import com.codeit.team7.findex.dto.PatchSyncConfigCommand;
import com.codeit.team7.findex.dto.SyncConfigDto;
import com.codeit.team7.findex.mapper.syncjob.SyncConfigMapper;
import com.codeit.team7.findex.repository.IndexInfoRepository;
import com.codeit.team7.findex.service.SyncConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SyncConfigServiceImpl implements SyncConfigService {

  private final IndexInfoRepository indexInfoRepository;
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
}
