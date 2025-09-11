package com.codeit.team7.findex.service;

import com.codeit.team7.findex.domain.enums.PeriodType;
import com.codeit.team7.findex.dto.IndexDataScrollRequest;
import com.codeit.team7.findex.dto.PaginatedResult;
import com.codeit.team7.findex.dto.command.ExportCsvCommand;
import com.codeit.team7.findex.dto.command.IndexDataDto;
import com.codeit.team7.findex.dto.command.IndexDataQueryCommand;
import com.codeit.team7.findex.dto.request.IndexDataCreateRequest;
import com.codeit.team7.findex.dto.request.IndexDataUpdateRequest;
import com.codeit.team7.findex.dto.response.CursorPageResponseIndexDataDto;
import com.codeit.team7.findex.dto.response.IndexChartDto;
import com.codeit.team7.findex.dto.response.IndexPerformanceRankDto;
import com.codeit.team7.findex.dto.response.IndexPerformanceRankDto.IndexPerformanceDto;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface IndexDataService {

  IndexDataDto create(IndexDataCreateRequest request);

  PaginatedResult<IndexDataDto> getIndexDataList(IndexDataQueryCommand command);

  CursorPageResponseIndexDataDto getIndexData(IndexDataScrollRequest request);

  IndexChartDto getChartData(Long id, PeriodType periodType);

  IndexDataDto update(Long id, IndexDataUpdateRequest request);

  List<IndexPerformanceRankDto> getPerformanceRank(PeriodType periodType, int limit);

  void deleteById(Long id);

  List<IndexPerformanceDto> getFavoritePerformance(PeriodType periodType);

  ResponseEntity<byte[]> exportCsv(ExportCsvCommand command);
}
