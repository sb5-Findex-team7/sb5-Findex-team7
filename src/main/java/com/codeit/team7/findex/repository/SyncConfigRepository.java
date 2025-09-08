package com.codeit.team7.findex.repository;

import com.codeit.team7.findex.domain.entity.SyncConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncConfigRepository extends JpaRepository<SyncConfig, Long> {

}
