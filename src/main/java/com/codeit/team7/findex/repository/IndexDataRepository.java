package com.codeit.team7.findex.repository;

import com.codeit.team7.findex.domain.entity.IndexData;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexDataRepository extends JpaRepository<IndexData, Long> {

  List<IndexData> findByIndexInfoIdOrderByBaseDateAsc(Long indexInfoId);

  @Query("SELECT d FROM IndexData d " +
      "WHERE d.sourceType = :periodType " +
      "ORDER BY d.versus DESC")
  List<IndexData> findLatestDataByPeriodType(@Param("periodType") String periodType,
      Pageable pageable);

  @Query("SELECT d FROM IndexData d " +
      "WHERE d.indexInfo.favorite = true " +
      "AND d.sourceType = :periodType " +
      "ORDER BY d.versus DESC")
  List<IndexData> findFavoriteData(@Param("periodType") String periodType);

}
