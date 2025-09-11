package com.codeit.team7.findex.repository;

import com.codeit.team7.findex.domain.entity.IndexData;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface IndexDataRepository extends JpaRepository<IndexData, Long> {

  @EntityGraph(attributePaths = {"indexInfo.id"})
  List<IndexData> findAllByIndexInfoIdInAndBaseDateBetween(List<Long> indexInfoIds,
      LocalDate startDate,
      LocalDate endDate);

  @EntityGraph(attributePaths = {"indexInfo.id"})
  List<IndexData> findAllByIndexInfoIdInAndBaseDate(List<Long> indexInfoId, LocalDate baseDate);



  @Repository
  public interface IndexDataRepository extends JpaRepository<IndexData, Long> {
  // 카테고리
  //  List<IndexData> findBySourceType(SourceType sourceType);


  boolean existsByIndexInfoIdAndBaseDate(@Param("indexInfoId") Long indexInfoId,
                                         @Param("baseDate") LocalDate baseDate);
}
