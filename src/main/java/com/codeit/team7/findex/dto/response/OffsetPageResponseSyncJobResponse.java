package com.codeit.team7.findex.dto.response;

import com.codeit.team7.findex.dto.SyncJobDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class OffsetPageResponseSyncJobResponse {

  private final List<SyncJobDto> content;
  private final int size;
  private final long totalPages;
  private final int pageNum;

}
