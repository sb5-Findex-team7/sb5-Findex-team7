package com.codeit.team7.findex.repository;

import com.codeit.team7.findex.domain.entity.IndexData;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndexDataRepository extends JpaRepository<IndexData, Long> {

  @EntityGraph(attributePaths = {"indexInfo.id"})
  List<IndexData> findAllByIndexInfoIdInAndBaseDateBetween(List<Long> indexInfoIds,
      LocalDate startDate,
      LocalDate endDate);


}
