package com.codeit.team7.findex.dto.command;

import com.codeit.team7.findex.domain.enums.SortedDirection;
import com.codeit.team7.findex.domain.enums.SyncConfigSoredField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class GetSyncConfigCommand {

  private final Long indexInfoId;
  private final Boolean enabled;
  private final Long idAfter;
  private final String cursor;
  private final SyncConfigSoredField sortField;
  private final SortedDirection sortDirection;
  private final int size;
}
