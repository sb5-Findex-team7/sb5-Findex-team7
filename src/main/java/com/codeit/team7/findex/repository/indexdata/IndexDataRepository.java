package com.codeit.team7.findex.repository.indexdata;

import com.codeit.team7.findex.domain.entity.IndexData;
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

  List<IndexData> findByIndexInfoIdOrderByBaseDateAsc(Long indexInfoId);

  List<IndexData> findAllByBaseDateBetween(LocalDate startDate, LocalDate endDate);

  @EntityGraph(attributePaths = {"indexInfo"})
  List<IndexData> findByIndexInfoIsFavoriteAndBaseDateBetween(
      boolean favorite, LocalDate startDate, LocalDate endDate
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
