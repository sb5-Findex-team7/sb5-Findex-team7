package com.codeit.team7.findex.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Builder
@ToString //todo 지워야함
public class SyncConfigDto {

  private Long id;
  private Long indexInfoId;
  private String indexClassification;
  private String indexName;
  private Boolean enabled;
}
