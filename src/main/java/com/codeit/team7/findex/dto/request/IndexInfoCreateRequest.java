package com.codeit.team7.findex.dto.request;

import com.codeit.team7.findex.domain.entity.IndexInfo;
import com.codeit.team7.findex.domain.entity.SourceType;
import java.math.BigDecimal;
import java.time.LocalDate;

public record IndexInfoCreateRequest(
    String indexClassification,
    String indexName,
    Integer employedItemsCount,
    LocalDate basePointInTime,
    BigDecimal baseIndex,
    Boolean favorite
) {

  public IndexInfo toEntity() {
    return new IndexInfo(
        indexClassification,
        indexName,
        employedItemsCount,
        basePointInTime,
        baseIndex,
        SourceType.USER,
        favorite != null && favorite,
        true
    );
  }
}
