package com.codeit.team7.findex.repository.indexdata;

import com.codeit.team7.findex.domain.entity.IndexData;
import java.time.LocalDate;
import java.util.List;

public interface IndexDataRepositoryCustom {

  List<IndexData> findByCursorAndFilters(
      Long indexInfoId,
      LocalDate startDate,
      LocalDate endDate,
      LocalDate cursorDate,
      Long idAfter,
      String sortDirection,
      int pageSizePlusOne);
}

