package com.codeit.team7.findex.controller;


import com.codeit.team7.findex.domain.enums.PeriodType;
import com.codeit.team7.findex.dto.PaginatedResult;
import com.codeit.team7.findex.dto.command.ExportCsvCommand;
import com.codeit.team7.findex.dto.command.IndexDataDto;
import com.codeit.team7.findex.dto.command.IndexDataQueryCommand;
import com.codeit.team7.findex.dto.request.IndexDataCreateRequest;
import com.codeit.team7.findex.dto.request.IndexDataUpdateRequest;
import com.codeit.team7.findex.dto.response.IndexChartDto;
import com.codeit.team7.findex.dto.response.IndexPerformanceRankDto;
import com.codeit.team7.findex.service.IndexDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Index Data", description = "지수 데이터 API")
@RestController
@RequestMapping("/api/index-data")
@RequiredArgsConstructor
@Tag(name = "지수 데이터 API", description = "지수 데이터 관리 API")
public class IndexDataController {

  private final IndexDataService indexDataService;


  @GetMapping
  public ResponseEntity<PaginatedResult<IndexDataDto>> getIndexData(
      @RequestParam(required = false) Long indexInfoId,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
      @RequestParam(required = false) Long idAfter,
      @RequestParam(required = false) String cursor,
      @RequestParam(defaultValue = "baseDate") String sortField,
      @RequestParam(defaultValue = "desc") String sortDirection,
      @RequestParam(defaultValue = "10") int size
  ) {
    IndexDataQueryCommand command = IndexDataQueryCommand.builder()
                                                         .indexInfoId(indexInfoId)
                                                         .startDate(startDate)
                                                         .endDate(endDate)
                                                         .idAfter(idAfter)
                                                         .cursor(cursor)
                                                         .sortField(sortField)
                                                         .sortDirection(sortDirection)
                                                         .size(size)
                                                         .build();

    return ResponseEntity.ok(indexDataService.getIndexDataList(command));
  }

  @Operation(summary = "차트 데이터 조회")
  @GetMapping("/{id}/chart")
  public ResponseEntity<List<IndexChartDto>> getChartData(
      @PathVariable Long id,
      @RequestParam(defaultValue = "DAILY") PeriodType periodType) {
    return ResponseEntity.ok(indexDataService.getChartData(id, periodType));
  }

  @Operation(summary = "성과 랭킹 조회")
  @GetMapping("/performance/rank")
  public ResponseEntity<List<IndexPerformanceRankDto>> getPerformanceRank(
      @RequestParam(defaultValue = "WEEKLY") PeriodType periodType,
      @RequestParam(defaultValue = "10") int limit) {

    return ResponseEntity.ok(indexDataService.getPerformanceRank(periodType, limit));
  }

  @PostMapping
  @Operation(summary = "지수 데이터 등록")
  public ResponseEntity<IndexDataDto> create(
      @Valid @RequestBody IndexDataCreateRequest request) {

    IndexDataDto dto = indexDataService.create(request);

    return ResponseEntity.ok(dto);
  }

  @PatchMapping("/{id}")
  @Operation(summary = "지수 데이터 수정")
  public ResponseEntity<IndexDataDto> update(@PathVariable Long id,
      @RequestBody IndexDataUpdateRequest idur) {
    IndexDataDto updated = indexDataService.update(id, idur);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updated);
  }

  @DeleteMapping(path = "/{id}")
  @Operation(summary = "지수 데이터 삭제")
  public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    indexDataService.deleteById(id);
    return ResponseEntity
        .status(HttpStatus.OK)
        .build();
  }

  @Operation(summary = "즐겨찾기 지수 성과 조회")
  @GetMapping("/performance/favorite")
  public ResponseEntity<List<IndexPerformanceRankDto.IndexPerformanceDto>> getFavoritePerformance(
      @RequestParam(defaultValue = "WEEKLY") PeriodType periodType) {
    return ResponseEntity.ok(indexDataService.getFavoritePerformance(periodType));
  }

  @Operation(summary = "CSV 다운로드")
  @GetMapping("/export/csv")
  public ResponseEntity<byte[]> exportCsv(
      @RequestParam(required = false) Long indexInfoId,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
      @RequestParam(required = false, defaultValue = "baseDate") String sortField,
      @RequestParam(required = false, defaultValue = "desc") String sortDirection) {

    ExportCsvCommand command = ExportCsvCommand.builder()
                                               .indexInfoId(indexInfoId)
                                               .startDate(startDate)
                                               .endDate(endDate)
                                               .sortField(sortField)
                                               .sortDirection(sortDirection)
                                               .build();

    return indexDataService.exportCsv(command);
  }
}