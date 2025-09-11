package com.codeit.team7.findex.repository;

import com.codeit.team7.findex.domain.entity.IndexInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndexInfoRepository extends JpaRepository<IndexInfo, Long>, IndexInfoQueryRepository {

  List<IndexInfo> findAllByIndexClassificationIn(List<String> indexClassifications);

  List<IndexInfo> findAllByIdIn(List<Long> ids);

  List<IndexInfo> findAllByEnabled(Boolean enabled);
}
