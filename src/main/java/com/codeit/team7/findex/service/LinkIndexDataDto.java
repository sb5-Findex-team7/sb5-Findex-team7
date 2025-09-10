package com.codeit.team7.findex.service;

import com.codeit.team7.findex.dto.response.StockMarketIndexResponse.Item;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class LinkIndexDataDto {

  Map<Long, List<Item>> items;
  boolean isToUpdate;
  LocalDate baseFromDate;
  LocalDate baseToDate;
  String ip;
}
