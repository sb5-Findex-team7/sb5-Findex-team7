package com.codeit.team7.findex.repository;

import com.codeit.team7.findex.domain.entity.IndexData;
import com.codeit.team7.findex.domain.enums.IndexDataSortDirection;
import com.codeit.team7.findex.domain.enums.IndexDataSortField;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexDataQueryRepository {
  List<IndexData> fetchPage(
      Long indexInfoId,
      LocalDate startDate,
      LocalDate endDate,
      Long idAfter,
      IndexDataSortField sortField,
      IndexDataSortDirection sortDirection,
      int size
  );
}
