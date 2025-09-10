package com.codeit.team7.findex.repository;

import com.codeit.team7.findex.domain.entity.SyncJob;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncJobRepository extends JpaRepository<SyncJob, Long>, SyncJobQueryRepository {

  Optional<SyncJob> findFirstByJobTimeBetweenAndJobTypeAndWorkerNot(Instant startTime,
      Instant endTime,
      String jobType, String worker);

  @EntityGraph(attributePaths = {"indexInfo.id"})
  List<SyncJob> findAllByJobTimeBetweenAndJobType(Instant startTime, Instant endTime,
      String jobType);

  @EntityGraph(attributePaths = {"indexInfo.id"})
  List<SyncJob> findAllByTargetDateBetweenAndJobType(LocalDate startDate, LocalDate endDate,
      String jobType);


  Optional<SyncJob> findTopByTargetDateAndWorkerAndJobType(
      LocalDate targetDate, String worker, String jobType);

  //
  List<SyncJob> findAllByTargetDateAndWorkerAndJobType(
      LocalDate targetDate, String worker, String jobType);

  @Query("""
      SELECT sj
      FROM SyncJob sj
      JOIN FETCH sj.indexInfo
      WHERE sj.indexInfo.id IN :indexInfoIds
      AND sj.targetDate BETWEEN :startDate AND :endDate
      AND sj.jobType = :jobType
      """)
  List<SyncJob> findAllByIndexInfoIdInAndTargetDateBetweenAndJobType(
      @Param("indexInfoIds") List<Long> indexInfoIds,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate,
      @Param("jobType") String jobType
  );

}
