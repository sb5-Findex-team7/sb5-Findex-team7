package com.codeit.team7.findex.service;

import com.codeit.team7.findex.domain.enums.SourceType;
import com.codeit.team7.findex.dto.command.IndexDataDto;
import com.codeit.team7.findex.dto.request.IndexDataCreateRequest;
import com.codeit.team7.findex.dto.request.IndexDataUpdateRequest;
import com.codeit.team7.findex.dto.response.CursorPageResponseIndexDataDto;
import java.time.LocalDateTime;

public interface IndexDataService {

  IndexDataDto create(IndexDataCreateRequest request);

  IndexDataDto update(Long id, IndexDataUpdateRequest request);

  void deleteById(Long id);
}
