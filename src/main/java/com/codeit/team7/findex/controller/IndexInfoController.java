package com.codeit.team7.findex.controller;

import com.codeit.team7.findex.dto.CursorPageResponseIndexInfoDto;
import com.codeit.team7.findex.dto.command.IndexInfoDto;
import com.codeit.team7.findex.dto.request.IndexInfoCreateRequest;
import com.codeit.team7.findex.dto.request.IndexInfoUpdateRequest;
import com.codeit.team7.findex.dto.response.IndexInfoSummaryDto;
import com.codeit.team7.findex.service.IndexInfoService;
import java.util.List;
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
@RequiredArgsConstructor
@RequestMapping("/api/index-infos")
public class IndexInfoController {

  private final IndexInfoService indexInfoService;

  @PostMapping
  public ResponseEntity<IndexInfoDto> create(
      @RequestBody IndexInfoCreateRequest indexInfoCreateRequest) {
    IndexInfoDto indexInfoDto = indexInfoService.create(indexInfoCreateRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(indexInfoDto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<IndexInfoDto> get(@PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(indexInfoService.findById(id));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<IndexInfoDto> update(
      @PathVariable Long id,
      @RequestBody IndexInfoUpdateRequest indexInfoUpdateRequest) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(indexInfoService.update(id, indexInfoUpdateRequest));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    indexInfoService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/summaries")
  public ResponseEntity<List<IndexInfoSummaryDto>> summaries() {
    return ResponseEntity.status(HttpStatus.OK).body(indexInfoService.findAllSummaries());
  }

  @GetMapping
  public ResponseEntity<CursorPageResponseIndexInfoDto> list(
      @RequestParam(required = false) String indexClassification,
      @RequestParam(required = false) String indexName,
      @RequestParam(required = false) Boolean favorite,
      @RequestParam(defaultValue = "indexClassification") String sortField,
      @RequestParam(defaultValue = "asc") String sortDirection,
      @RequestParam(required = false) Long idAfter,
      @RequestParam(required = false) String cursor,
      @RequestParam(defaultValue = "10") int size
  ) {
    CursorPageResponseIndexInfoDto body = indexInfoService.search(
        indexClassification, indexName, favorite,
        sortField, sortDirection, idAfter, cursor, size
    );
    return ResponseEntity.status(HttpStatus.OK).body(body);
  }
}
