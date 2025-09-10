package com.codeit.team7.findex.service;

import com.codeit.team7.findex.domain.enums.PeriodType;
import com.codeit.team7.findex.dto.PaginatedResult;
import com.codeit.team7.findex.dto.command.ExportCsvCommand;
import com.codeit.team7.findex.dto.command.IndexDataQueryCommand;
import com.codeit.team7.findex.dto.response.IndexChartDto;
import com.codeit.team7.findex.dto.response.IndexDataDto;
import com.codeit.team7.findex.dto.response.IndexPerformanceRankDto;
import com.codeit.team7.findex.dto.response.IndexPerformanceRankDto.IndexPerformanceDto;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface IndexDataService {

  PaginatedResult<IndexDataDto> getIndexDataList(IndexDataQueryCommand command);

  List<IndexChartDto> getChartData(Long id, PeriodType periodType);

  List<IndexPerformanceRankDto> getPerformanceRank(PeriodType periodType, int limit);

  List<IndexPerformanceDto> getFavoritePerformance(PeriodType periodType);

  ResponseEntity<byte[]> exportCsv(ExportCsvCommand command);
}
