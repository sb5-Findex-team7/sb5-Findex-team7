package com.codeit.team7.findex.dto.command;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IndexDataQueryCommand {
  private Long indexInfoId;
  private LocalDate startDate;
  private LocalDate endDate;
  private String cursor;
  private Long idAfter;
  private String sortField;
  private String sortDirection;
  private int size;
}