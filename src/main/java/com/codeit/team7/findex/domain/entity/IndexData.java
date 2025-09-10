package com.codeit.team7.findex.domain.entity;

import com.codeit.team7.findex.domain.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "index_data")
public class IndexData extends BaseUpdatableEntity {

  @ManyToOne
  @JoinColumn(name = "index_info_id")
  private IndexInfo indexInfo;
  @Column(nullable = false, name = "bas_dt")
  private LocalDate baseDate;
  @Column(nullable = false, name = "source_type")
  private String sourceType;
  @Column(nullable = false, name = "mkp")
  private BigDecimal marketPrice;
  @Column(nullable = false, name = "clpr")
  private BigDecimal closingPrice;
  @Column(nullable = false, name = "hipr")
  private BigDecimal highPrice;
  @Column(nullable = false, name = "lopr")
  private BigDecimal lowPrice;
  @Column(nullable = false, name = "vs")
  private BigDecimal versus;
  @Column(nullable = false, name = "flt_rt")
  private BigDecimal fluctuationRate;
  @Column(nullable = false, name = "trqu")
  private Long tradingQuantity;
  @Column(nullable = false, name = "tr_prc")
  private Long tradingPrice;
  @Column(nullable = false, name = "lstg_mrkt_tot_amt")
  private Long marketTotalAmount;
}
