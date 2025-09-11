package com.codeit.team7.findex.dto;

import com.codeit.team7.findex.dto.response.StockMarketIndexResponse.Item;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class GetNewIndexInfosResult {

  List<Item> items;
  boolean isToUpdate;
  LocalDate BaseDate;

}
