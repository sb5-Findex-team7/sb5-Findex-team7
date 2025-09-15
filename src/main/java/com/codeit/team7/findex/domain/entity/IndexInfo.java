package com.codeit.team7.findex.domain.entity;

import com.codeit.team7.findex.domain.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// @AllArgsConstructor
// @NoArgsConstructor(access = AccessLevel.PROTECTED)
// @NoArgsConstructor
// @Table(name = "index_info")
//    ,
//    uniqueConstraints = {
//        @UniqueConstraint(columnNames = {"idx_csf", "idx_nm"})
//    }
@Getter
@Entity
@Setter
@Table(name = "index_info")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IndexInfo extends BaseUpdatableEntity {

  @Column(nullable = false, name = "idx_csf", length = 300)
  private String indexClassification;

  @Column(nullable = false, name = "idx_nm", length = 200)
  private String indexName;

  @Column(nullable = false, name = "itms_cnt")
  private Integer itemCount;

  @Column(nullable = false, name = "bas_pntm")
  private LocalDate basePointInTime;
  @Column(nullable = false, name = "bas_idx")
  private BigDecimal baseIndex;
  @Column(nullable = false, name = "source_type")
  private String sourceType;
  @Column(nullable = false, name = "favorite")
  private Boolean favorite;
  @Column(nullable = false, name = "enabled")
  @Builder.Default
  private Boolean enabled = true;

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
