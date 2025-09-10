package com.codeit.team7.findex.controller;

import com.codeit.team7.findex.domain.enums.IndexDataSortDirection;
import com.codeit.team7.findex.domain.enums.IndexDataSortField;
import com.codeit.team7.findex.domain.enums.SourceType;
import com.codeit.team7.findex.dto.IndexDataScrollRequest;
import com.codeit.team7.findex.dto.command.IndexDataDto;
import com.codeit.team7.findex.dto.request.IndexDataCreateRequest;
import com.codeit.team7.findex.dto.request.IndexDataUpdateRequest;
import com.codeit.team7.findex.dto.response.CursorPageResponseIndexDataDto;
import com.codeit.team7.findex.service.IndexDataService;
import com.codeit.team7.findex.service.impl.IndexDataServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/index-data")
@RequiredArgsConstructor
@Tag(name = "지수 데이터 API", description = "지수 데이터 관리 API")
public class IndexDataController {
  private final IndexDataService indexDataService;
  private final IndexDataServiceImpl indexDataServiceImpl;

  @PostMapping
  @Operation(summary = "지수 데이터 등록")
  public ResponseEntity<IndexDataDto> create(
      @Valid @RequestBody IndexDataCreateRequest request,
      @RequestParam("type") SourceType type,
      UriComponentsBuilder uriBuilder) {

    IndexDataDto dto = indexDataService.create(request, type);

    return ResponseEntity
        .created(uriBuilder.path("/api/index-data/{id}")
            .buildAndExpand(dto.getId())
            .toUri())
        .body(dto);
  }

  @GetMapping(path = "{id}")
  @Operation(summary = "지수 데이터 조회")
  public ResponseEntity<IndexDataDto> findByIndexInfoId(@PathVariable Long id) {
    IndexDataDto res = indexDataService.findByIndexInfoId(id);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(res);
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


  @GetMapping
  public ResponseEntity<CursorPageResponseIndexDataDto> getIndexData(
      @RequestParam(required = false) Long indexInfoId,
      @RequestParam(required = false) LocalDate startDate,
      @RequestParam(required = false) LocalDate endDate,
      @RequestParam(required = false) Long idAfter,
      @RequestParam(required = false) String cursor,
      @RequestParam(required = false) IndexDataSortField sortField,
      @RequestParam(required = false) IndexDataSortDirection sortDirection,
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




}
