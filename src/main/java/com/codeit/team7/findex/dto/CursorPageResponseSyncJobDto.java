package com.codeit.team7.findex.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@AllArgsConstructor
@Getter
@Builder
public class CursorPageResponseSyncJobDto {


  private final Slice<SyncJobDto> content;
  private final String nextCursor;
  private final Long nextIdAfter;
  private final int size;
  private final Long totalElements;
  private final boolean hasNext;
}
