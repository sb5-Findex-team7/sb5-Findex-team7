package com.codeit.team7.findex.repository;

import com.codeit.team7.findex.domain.entity.IndexInfo;
import com.codeit.team7.findex.domain.enums.SortedDirection;
import com.codeit.team7.findex.domain.enums.SyncConfigSoredField;
import com.codeit.team7.findex.dto.PaginatedResult;

public interface SyncConfigQueryRepository {

  PaginatedResult<IndexInfo> searchSyncConfig(
      Long IndexInfoId,
      Boolean enabled,
      Long idAfter,
      String cursor,
      SyncConfigSoredField sortField,
      SortedDirection sortDirection,
      int size
  );

}
