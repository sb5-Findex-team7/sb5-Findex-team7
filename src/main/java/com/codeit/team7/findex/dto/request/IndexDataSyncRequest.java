package com.codeit.team7.findex.dto.request;

import java.time.LocalDate;
import java.util.List;

public class IndexDataSyncRequest {

  private List<Long> indexInfoIds;
  private LocalDate baseDateFrom;
  private LocalDate baseDateTo;
}
