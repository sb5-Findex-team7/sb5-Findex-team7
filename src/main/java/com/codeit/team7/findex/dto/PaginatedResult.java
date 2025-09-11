package com.codeit.team7.findex.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class PaginatedResult<T> {

  private final List<T> content;
  private final Long totalElements;
  private final Boolean hasNext;
}
