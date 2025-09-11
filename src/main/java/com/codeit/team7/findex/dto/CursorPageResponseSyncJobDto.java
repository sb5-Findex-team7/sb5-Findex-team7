package com.codeit.team7.findex.dto;

import com.codeit.team7.findex.dto.command.SyncJobDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class CursorPageResponseSyncJobDto {

  private final List<SyncJobDto> content;
  private final LocalDateTime nextCursor;
  private final Long nextIdAfter;
  private final int size;
  private final Long totalElements;
  private final boolean hasNext;
}
