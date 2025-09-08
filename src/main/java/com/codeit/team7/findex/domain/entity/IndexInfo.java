package com.codeit.team7.findex.domain.entity;

import com.codeit.team7.findex.domain.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "index_info")
public class IndexInfo extends BaseUpdatableEntity {

  @Column(nullable = false, name = "idx_csf")
  private String indexClassification;
  @Column(nullable = false, name = "idx_nm")
  private String indexName;
  @Column(nullable = false, name = "itms_cnt")
  private Integer itemCount;
  @Column(nullable = false, name = "bas_pntm")
  private LocalDate basePointInTime;
  @Column(nullable = false, name = "bas_idx")
  private Integer baseIndex;
  @Column(nullable = false, name = "source_type")
  private String sourceType;
  @Column(nullable = false, name = "favorite")
  private Boolean favorite;
  @Column(name = "enabled")
  private Boolean enabled;
}
