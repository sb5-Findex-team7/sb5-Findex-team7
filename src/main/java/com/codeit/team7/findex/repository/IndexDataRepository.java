package com.codeit.team7.findex.repository;

import com.codeit.team7.findex.domain.entity.IndexData;
import com.codeit.team7.findex.repository.indexdata.IndexDataRepositoryCustom;
import com.codeit.team7.findex.repository.indexdata.IndexPerformanceProjection;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexDataRepository extends JpaRepository<IndexData, Long>,
    IndexDataRepositoryCustom {

  @EntityGraph(attributePaths = {"indexInfo.id"})
  List<IndexData> findAllByIndexInfoIdInAndBaseDateBetween(List<Long> indexInfoIds,
      LocalDate startDate,
      LocalDate endDate);

  @EntityGraph(attributePaths = {"indexInfo.id"})
  List<IndexData> findAllByIndexInfoIdInAndBaseDate(List<Long> indexInfoId, LocalDate baseDate);

  // 카테고리
  //  List<IndexData> findBySourceType(SourceType sourceType);

  boolean existsByIndexInfoIdAndBaseDate(@Param("indexInfoId") Long indexInfoId,
      @Param("baseDate") LocalDate baseDate);


  List<IndexData> findByIndexInfoIdAndBaseDateBetweenOrderByBaseDateAsc(Long indexInfoId,
      LocalDate startDate, LocalDate endDate);

  @EntityGraph(attributePaths = {"indexInfo"})
  List<IndexData> findByBaseDateBetween(LocalDate startDate, LocalDate endDate);

  @EntityGraph(attributePaths = {"indexInfo"})
  List<IndexData> findByIndexInfoFavoriteAndBaseDateBetween(
      boolean favorite, LocalDate startDate, LocalDate endDate
  );

  @Query(value = """
      SELECT
          ii.id                AS index_info_id,
          ii.idx_csf           AS index_classification,
          ii.idx_nm            AS index_name,
          (last.clpr - first.clpr) AS versus,
          ROUND(((last.clpr - first.clpr) / first.clpr) * 100, 4) AS fluctuation_rate,
          last.clpr            AS current_price,
          first.clpr           AS before_price
      FROM index_info ii
      JOIN (
          SELECT idata.index_info_id, MIN(idata.bas_dt) AS min_date, MAX(idata.bas_dt) AS max_date
          FROM index_data idata
          WHERE idata.bas_dt BETWEEN :startDate AND :endDate
          GROUP BY idata.index_info_id
      ) agg ON agg.index_info_id = ii.id
      JOIN index_data first ON first.index_info_id = ii.id AND first.bas_dt = agg.min_date
      JOIN index_data last  ON last.index_info_id  = ii.id AND last.bas_dt  = agg.max_date
      """, nativeQuery = true)
  List<IndexPerformanceProjection> findIndexPerformanceBetween(
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate
  );


  @Query(value = """
      SELECT
          ii.id                AS index_info_id,
          ii.idx_csf           AS index_classification,
          ii.idx_nm            AS index_name,
          (last.clpr - first.clpr) AS versus,
          ROUND(((last.clpr - first.clpr) / first.clpr) * 100, 4) AS fluctuation_rate,
          last.clpr            AS current_price,
          first.clpr           AS before_price
      FROM index_info ii
      JOIN (
          SELECT idata.index_info_id, MIN(idata.bas_dt) AS min_date, MAX(idata.bas_dt) AS max_date
          FROM index_data idata
          WHERE idata.bas_dt BETWEEN :startDate AND :endDate
          GROUP BY idata.index_info_id
      ) agg ON agg.index_info_id = ii.id
      JOIN index_data first ON first.index_info_id = ii.id AND first.bas_dt = agg.min_date
      JOIN index_data last  ON last.index_info_id  = ii.id AND last.bas_dt  = agg.max_date
      WHERE ii.favorite = true
      """, nativeQuery = true)
  List<IndexPerformanceProjection> findIndexPerformanceBetweenWithFavorite(
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate
  );


  @Query("""
          SELECT i FROM IndexData i
          WHERE (:indexInfoId IS NULL OR i.indexInfo.id = :indexInfoId)
            AND (:startDate IS NULL OR i.baseDate >= :startDate)
            AND (:endDate IS NULL OR i.baseDate <= :endDate)
          ORDER BY
            CASE WHEN :sortField = 'baseDate' AND :sortDirection = 'ASC' THEN i.baseDate END ASC,
            CASE WHEN :sortField = 'baseDate' AND :sortDirection = 'DESC' THEN i.baseDate END DESC,
            CASE WHEN :sortField = 'marketPrice' AND :sortDirection = 'ASC' THEN i.marketPrice END ASC,
            CASE WHEN :sortField = 'marketPrice' AND :sortDirection = 'DESC' THEN i.marketPrice END DESC,
            CASE WHEN :sortField = 'closingPrice' AND :sortDirection = 'ASC' THEN i.closingPrice END ASC,
            CASE WHEN :sortField = 'closingPrice' AND :sortDirection = 'DESC' THEN i.closingPrice END DESC
      """)
  List<IndexData> findByFilters(
      @Param("indexInfoId") Long indexInfoId,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate,
      @Param("sortField") String sortField,
      @Param("sortDirection") String sortDirection
  );

}
