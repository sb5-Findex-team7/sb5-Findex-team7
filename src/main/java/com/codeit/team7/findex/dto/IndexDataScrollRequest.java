package com.codeit.team7.findex.dto;

import com.codeit.team7.findex.domain.enums.IndexDataSortDirection;
import com.codeit.team7.findex.domain.enums.IndexDataSortField;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Builder
public class IndexDataScrollRequest {
  private Long indexInfoId;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime startTime;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime endTime;
  private Long idAfter;
  private String cursor;
  private IndexDataSortField sortField;
  private IndexDataSortDirection sortDirection;
  private Integer size;

  public int pageSizeOrDefault() {
    int s = (size == null ? 10 : size);
    return Math.max(1, Math.min(s, 100));
  }
  public IndexDataSortField sortFieldOrDefault() {
    return sortField == null ? IndexDataSortField.BASE_DATE : sortField;
  }
  public IndexDataSortDirection sortDirectionOrDefault() {
    return sortDirection == null ? IndexDataSortDirection.DESC : sortDirection;
  }
}
