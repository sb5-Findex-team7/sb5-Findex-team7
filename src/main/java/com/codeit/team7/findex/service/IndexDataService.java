package com.codeit.team7.findex.service;

import com.codeit.team7.findex.dto.IndexDataScrollRequest;
import com.codeit.team7.findex.dto.command.IndexDataDto;
import com.codeit.team7.findex.dto.request.IndexDataCreateRequest;
import com.codeit.team7.findex.dto.request.IndexDataUpdateRequest;
import com.codeit.team7.findex.dto.response.CursorPageResponseIndexDataDto;

public interface IndexDataService {

  IndexDataDto create(IndexDataCreateRequest request);

  CursorPageResponseIndexDataDto getIndexData(IndexDataScrollRequest request);

  IndexDataDto update(Long id, IndexDataUpdateRequest request);

  void deleteById(Long id);
}
