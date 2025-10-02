package com.codeit.team7.findex.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class PageResponseSyncJobDto {

  private final List<SyncJobDto> content;
  private final int size;
  private final long totalPages;
  private final int pageNum;
}
