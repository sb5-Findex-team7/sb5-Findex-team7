package com.codeit.team7.findex.domain.entity;

import com.codeit.team7.findex.domain.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "sync_job")
public class SyncJob extends BaseUpdatableEntity {

  @ManyToOne
  @JoinColumn(name = "sync_config_id")
  private SyncConfig syncConfig;
  @Column(nullable = false, name = "job_type")
  private String jobType;
  @Column(nullable = false, name = "target_dt")
  private LocalDate targetDate;
  @Column(nullable = false, name = "worker")
  private String worker;
  @Column(nullable = false, name = "job_time")
  private Instant jobTime;
  @Column(nullable = false, name = "is_completed")
  private Boolean isCompleted;
}
