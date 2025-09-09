package com.codeit.team7.findex.controller;

import com.codeit.team7.findex.dto.response.IndexChartDto;
import com.codeit.team7.findex.dto.response.IndexPerformanceRankDto;
import com.codeit.team7.findex.service.IndexDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Index Data", description = "지수 데이터 API")
@RestController
@RequestMapping("/api/index-data")
@RequiredArgsConstructor
public class IndexDataController {

  private final IndexDataService indexDataService;

  @Operation(summary = "차트 데이터 조회")
  @GetMapping("/{id}/chart")
  public ResponseEntity<List<IndexChartDto>> getChartData(
      @PathVariable Long id,
      @RequestParam(defaultValue = "DAILY") String periodType) {
    return ResponseEntity.ok(indexDataService.getChartData(id, periodType));
  }

  @Operation(summary = "성과 랭킹 조회")
  @GetMapping("/performance/rank")
  public ResponseEntity<List<IndexPerformanceRankDto>> getPerformanceRank(
      @RequestParam(defaultValue = "WEEKLY") String periodType,
      @RequestParam(defaultValue = "10") int limit) {
    return ResponseEntity.ok(indexDataService.getPerformanceRank(periodType, limit));
  }

  @Operation(summary = "즐겨찾기 지수 성과 조회")
  @GetMapping("/performance/favorite")
  public ResponseEntity<List<IndexPerformanceRankDto.IndexPerformanceDto>> getFavoritePerformance(
      @RequestParam(defaultValue = "WEEKLY") String periodType) {
    return ResponseEntity.ok(indexDataService.getFavoritePerformance(periodType));
  }

  @Operation(summary = "CSV 다운로드")
  @GetMapping("/export/csv")
  public ResponseEntity<byte[]> exportCsv(
      @RequestParam(required = false) Long indexInfoId,
      @RequestParam(required = false) String startDate,
      @RequestParam(required = false) String endDate,
      @RequestParam(required = false) String sortField,
      @RequestParam(required = false) String sortDirection) {
    return indexDataService.exportCsv(indexInfoId, startDate, endDate, sortField, sortDirection);
  }
}