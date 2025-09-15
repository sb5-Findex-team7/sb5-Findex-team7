package com.codeit.team7.findex.dto.request;

import com.codeit.team7.findex.domain.entity.IndexInfo;
import com.codeit.team7.findex.domain.enums.SourceType;
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

    return IndexInfo.builder()
                    .indexClassification(indexClassification)
                    .indexName(indexName)
                    .itemCount(employedItemsCount)
                    .basePointInTime(basePointInTime)
                    .baseIndex(baseIndex)
                    .sourceType(SourceType.USER.name())
                    .favorite(favorite != null && favorite)
                    .enabled(true)
                    .build();
  }
}
