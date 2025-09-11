package com.codeit.team7.findex.domain.entity;

import com.codeit.team7.findex.domain.entity.base.BaseUpdatableEntity;
import com.codeit.team7.findex.domain.enums.SourceType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "index_info",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"idx_csf", "idx_nm"})
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IndexInfo extends BaseUpdatableEntity {

  @Column(nullable = false, name = "idx_csf", length = 300)
  private String indexClassification;

  @Column(nullable = false, name = "idx_nm", length = 200)
  private String indexName;

  @Column(nullable = false, name = "itms_cnt")
  private Integer itemCount;

  @Column(nullable = false, name = "bas_pntm")
  private LocalDate basePointInTime;

  @Column(nullable = false, name = "bas_idx", precision = 32, scale = 2)
  private BigDecimal baseIndex;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, name = "source_type", length = 20)
  private SourceType sourceType;

  // 즐겨찾기 기본값 false
  @Column(nullable = false, name = "favorite")
  private boolean favorite;

  // 활성화 기본값 true
  @Column(nullable = false, name = "enabled")
  private boolean enabled = true;

  public IndexInfo(String indexClassification, String indexName, Integer itemCount,
      LocalDate basePointInTime, BigDecimal baseIndex, SourceType sourceType, boolean favorite,
      boolean enabled) {
    this.indexClassification = indexClassification;
    this.indexName = indexName;
    this.itemCount = itemCount;
    this.basePointInTime = basePointInTime;
    this.baseIndex = baseIndex;
    this.sourceType = sourceType;
    this.favorite = favorite;
    this.enabled = enabled;
  }

  public void update(Integer newItemCount, LocalDate newBasePointInTime, BigDecimal newBaseIndex,
      boolean newFavorite) {
    if (newItemCount != null && !newItemCount.equals(this.itemCount)) {
      this.itemCount = newItemCount;
    }
    if (newBasePointInTime != null && !newBasePointInTime.equals(this.basePointInTime)) {
      this.basePointInTime = newBasePointInTime;
    }
    if (newBaseIndex != null && !newBaseIndex.equals(this.baseIndex)) {
      this.baseIndex = newBaseIndex;
    }
    this.favorite = newFavorite;
  }
}
