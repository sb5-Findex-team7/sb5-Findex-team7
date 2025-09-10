package com.codeit.team7.findex.dto.response;

import com.codeit.team7.findex.dto.SyncConfigDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class CursorPageResponseSyncConfigResponse {

  private final List<SyncConfigDto> content;
  private final String nextCursor;
  private final Long nextIdAfter;
  private final int size;
  private final Long totalElements;
  private final boolean hasNext;

}
