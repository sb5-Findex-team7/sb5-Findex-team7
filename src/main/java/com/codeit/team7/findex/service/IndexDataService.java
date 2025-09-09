package com.codeit.team7.findex.service;

import com.codeit.team7.findex.dto.response.IndexChartDto;
import com.codeit.team7.findex.dto.response.IndexPerformanceRankDto;
import com.codeit.team7.findex.dto.response.IndexPerformanceRankDto.IndexPerformanceDto;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface IndexDataService {

  List<IndexChartDto> getChartData(Long id, String periodType);

  List<IndexPerformanceRankDto> getPerformanceRank(String periodType, int limit);

  List<IndexPerformanceDto> getFavoritePerformance(String periodType);

  ResponseEntity<byte[]> exportCsv(Long indexInfoId, String startDate, String endDate, String sortField, String sortDirection);
}
