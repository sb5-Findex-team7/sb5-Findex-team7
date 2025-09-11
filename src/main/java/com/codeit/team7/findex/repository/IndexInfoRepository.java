package com.codeit.team7.findex.repository;

import com.codeit.team7.findex.domain.entity.IndexInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndexInfoRepository extends JpaRepository<IndexInfo, Long>, IndexInfoQueryRepository {

}
