package com.codeit.team7.findex.dto.command;

import com.codeit.team7.findex.domain.entity.IndexInfo;

public record IndexInfoSummaryDto(
    Long id,
    String indexClassification,
    String indexName
) {

  public static IndexInfoSummaryDto fromEntity(IndexInfo entity) {
    return new IndexInfoSummaryDto(
        entity.getId(),
        entity.getIndexClassification(),
        entity.getIndexName()
    );
  }
}
