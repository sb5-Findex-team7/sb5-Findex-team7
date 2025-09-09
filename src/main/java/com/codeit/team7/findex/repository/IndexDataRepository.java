package com.codeit.team7.findex.repository;

import com.codeit.team7.findex.domain.entity.IndexData;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface IndexDataRepository extends JpaRepository<IndexData, Long> {
  // 카테고리
//  List<IndexData> findBySourceType(SourceType sourceType);


  boolean existsByIndexInfoIdAndBaseDate(@Param("indexInfoId") Long indexInfoId,
                                         @Param("baseDate") LocalDate baseDate);
}
