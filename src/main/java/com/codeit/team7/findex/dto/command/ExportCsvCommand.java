package com.codeit.team7.findex.dto.command;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExportCsvCommand {

  private final Long indexInfoId;
  private final LocalDate startDate;
  private final LocalDate endDate;
  private final String sortField;
  private final String sortDirection;
}
