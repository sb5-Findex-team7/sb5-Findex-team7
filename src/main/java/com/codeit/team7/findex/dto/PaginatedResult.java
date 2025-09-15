package com.codeit.team7.findex.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class PaginatedResult<T> {

  private final List<T> content;
  private final String nextCursor;
  private final Long nextIdAfter;
  private final int size;
  private final Long totalElements;
  private final Boolean hasNext;
}
