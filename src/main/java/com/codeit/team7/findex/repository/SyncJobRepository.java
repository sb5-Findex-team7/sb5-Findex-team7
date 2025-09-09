package com.codeit.team7.findex.repository;

import com.codeit.team7.findex.domain.entity.SyncJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncJobRepository extends JpaRepository<SyncJob, Long>, SyncJobQueryRepository {


}
