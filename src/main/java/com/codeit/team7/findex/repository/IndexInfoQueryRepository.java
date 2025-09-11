package com.codeit.team7.findex.repository;

import com.codeit.team7.findex.domain.entity.IndexInfo;
import com.codeit.team7.findex.dto.PaginatedResult;

public interface IndexInfoQueryRepository {

  PaginatedResult<IndexInfo> search(
      String indexClassification,
      String indexName,
      Boolean favorite,
      String sortField,
      String sortDirection,
      String lastSortValue,
      Long lastId,
      int pageSize
  );
}
