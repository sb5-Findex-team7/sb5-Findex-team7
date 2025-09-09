package com.codeit.team7.findex.repository;

import com.codeit.team7.findex.domain.entity.SyncJob;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncJobRepository extends JpaRepository<SyncJob, Long>, SyncJobQueryRepository {

  Optional<SyncJob> findFirstByJobTimeBetweenAndJobTypeAndWorkerNot(Instant startTime,
      Instant endTime,
      String jobType, String worker);

  @EntityGraph(attributePaths = {"indexInfo.id"})
  List<SyncJob> findAllByJobTimeBetweenAndJobType(Instant startTime, Instant endTime,
      String jobType);

}
