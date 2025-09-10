package com.codeit.team7.findex.controller;


import com.codeit.team7.findex.domain.enums.IndexDataSortDirection;
import com.codeit.team7.findex.domain.enums.IndexDataSortField;
import com.codeit.team7.findex.dto.IndexDataScrollRequest;
import com.codeit.team7.findex.dto.command.IndexDataDto;
import com.codeit.team7.findex.dto.request.IndexDataCreateRequest;
import com.codeit.team7.findex.dto.request.IndexDataUpdateRequest;
import com.codeit.team7.findex.dto.response.CursorPageResponseIndexDataDto;
import com.codeit.team7.findex.service.IndexDataService;
import com.codeit.team7.findex.service.impl.IndexDataServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequestMapping("/api/index-data")
@RequiredArgsConstructor
@Tag(name = "지수 데이터 API", description = "지수 데이터 관리 API")
public class IndexDataController {
  private final IndexDataService indexDataService;
  private final IndexDataServiceImpl indexDataServiceImpl;


  @GetMapping
  @Operation(summary = "지수 데이터 목록 조회")
  public ResponseEntity<CursorPageResponseIndexDataDto> getIndexData(
      @Parameter(description = "지수 정보 ID")
      @RequestParam(required = false) Long indexInfoId,
      @Parameter(description = "시작 일자")
      @RequestParam(required = false) LocalDate startDate,
      @Parameter(description = "종료 일자")
      @RequestParam(required = false) LocalDate endDate,
      @Parameter(description = "이전 페이지 마지막 요소 ID")
      @RequestParam(required = false) Long idAfter,
      @Parameter(description = "커서 (다음 페이지 시작점)")
      @RequestParam(required = false) String cursor,
      @Parameter(schema = @Schema(type = "string", defaultValue = "baseDate"),
      description = "정렬 필드 (baseDate, marketPrice, closingPrice, highPrice, lowPrice, versus, fluctuationRate, tradingQuantity, tradingPrice, marketTotalAmount)")
      @RequestParam(required = false) IndexDataSortField sortField,
      @Parameter(schema = @Schema(type = "string", defaultValue = "desc"), description = "정렬 방향 (asc, desc)")
      @RequestParam(required = false) IndexDataSortDirection sortDirection,
      @Schema(defaultValue = "10", description = "페이지 크기")
      @RequestParam(required = false) Integer size) {
    int realSize = Optional.ofNullable(size)
        .filter(s -> s > 0)
        .orElse(10);

    LocalDateTime startTime = (startDate != null) ? startDate.atStartOfDay() : null;
    LocalDateTime endTime = (endDate != null) ? endDate.atStartOfDay() : null;

    CursorPageResponseIndexDataDto res = indexDataServiceImpl.getIndexData(
        IndexDataScrollRequest.builder()
            .indexInfoId(indexInfoId)
            .startTime(startTime)
            .endTime(endTime)
            .idAfter(idAfter)
            .sortField(sortField)
            .sortDirection(sortDirection)
            .size(realSize)
            .build()
    );
    return ResponseEntity.ok(res);
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

}
