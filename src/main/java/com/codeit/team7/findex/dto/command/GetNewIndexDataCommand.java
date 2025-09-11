package com.codeit.team7.findex.dto.command;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class GetNewIndexDataCommand {

  List<Long> indexInfoIds;
  LocalDate baseDateFrom;
  LocalDate baseDateTo;


}
