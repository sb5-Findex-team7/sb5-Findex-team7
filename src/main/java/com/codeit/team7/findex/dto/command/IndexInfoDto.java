package com.codeit.team7.findex.dto.command;

import com.codeit.team7.findex.domain.entity.IndexInfo;
import com.codeit.team7.findex.domain.entity.SourceType;
import java.math.BigDecimal;
import java.time.LocalDate;

public record IndexInfoDto(
    Long id,
    String indexClassification,
    String indexName,
    Integer employedItemsCount,
    LocalDate basePointInTime,
    BigDecimal baseIndex,
    SourceType sourceType,
    Boolean favorite
) {

  public static IndexInfoDto fromEntity(IndexInfo entity) {
    return new IndexInfoDto(
        entity.getId(),
        entity.getIndexClassification(),
        entity.getIndexName(),
        entity.getItemCount(),
        entity.getBasePointInTime(),
        entity.getBaseIndex(),
        entity.getSourceType(),
        entity.isFavorite()
    );
  }
}
