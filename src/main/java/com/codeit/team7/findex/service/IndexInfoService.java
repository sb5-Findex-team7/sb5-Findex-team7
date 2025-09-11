package com.codeit.team7.findex.service;

import com.codeit.team7.findex.dto.CursorPageResponseIndexInfoDto;
import com.codeit.team7.findex.dto.command.IndexInfoDto;
import com.codeit.team7.findex.dto.request.IndexInfoCreateRequest;
import com.codeit.team7.findex.dto.request.IndexInfoUpdateRequest;
import com.codeit.team7.findex.dto.response.IndexInfoSummaryDto;
import java.util.List;

public interface IndexInfoService {

  IndexInfoDto create(IndexInfoCreateRequest request);

  IndexInfoDto findById(Long id);

  IndexInfoDto update(Long Id, IndexInfoUpdateRequest request);

  void deleteById(Long id);

  List<IndexInfoSummaryDto> findAllSummaries();

  CursorPageResponseIndexInfoDto search(
      String indexClassification,
      String indexName,
      Boolean favorite,
      String sortField,
      String sortDirection,
      Long idAfter,
      String cursor,
      int size
  );
}
