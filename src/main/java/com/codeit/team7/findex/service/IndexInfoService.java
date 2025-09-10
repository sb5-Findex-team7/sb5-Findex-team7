package com.codeit.team7.findex.service;

import com.codeit.team7.findex.dto.command.IndexInfoDto;
import com.codeit.team7.findex.dto.command.IndexInfoSummaryDto;
import com.codeit.team7.findex.dto.request.IndexInfoCreateRequest;
import com.codeit.team7.findex.dto.request.IndexInfoUpdateRequest;
import java.util.List;

public interface IndexInfoService {
  IndexInfoDto create(IndexInfoCreateRequest request);

  IndexInfoDto findById(Long id);

  IndexInfoDto update(Long Id, IndexInfoUpdateRequest request);

  void deleteById(Long id);

  List<IndexInfoSummaryDto> findAllSummaries();
}
