package com.codeit.team7.findex.domain.entity;

import com.codeit.team7.findex.domain.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sync_config")
public class SyncConfig extends BaseUpdatableEntity {

  @OneToOne
  @JoinColumn(name = "index_info_id")
  private IndexInfo indexInfo;
  @Column(name = "enabled")
  private Boolean enabled;
}
