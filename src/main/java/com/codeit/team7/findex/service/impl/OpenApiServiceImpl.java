package com.codeit.team7.findex.service.impl;

import com.codeit.team7.findex.dto.request.StockMarketIndexRequest;
import com.codeit.team7.findex.dto.response.StockMarketIndexResponse;
import com.codeit.team7.findex.dto.response.StockMarketIndexResponse.Item;
import com.codeit.team7.findex.service.OpenApiService;
import com.codeit.team7.findex.util.OpenApiUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpenApiServiceImpl implements OpenApiService {

  private final OpenApiUtil openApiUtil;
  private static final String STARTING_TR_PRC = "20000000000000";
  private static final int MAX_ITEMS = 50;


  @Override
  public List<Item> GetNewIndexInfos() {

    StockMarketIndexResponse response = openApiUtil.fetchStockMarketIndex(
        StockMarketIndexRequest.builder()
            .beginTrPrc(STARTING_TR_PRC)
            .numOfRows(MAX_ITEMS)
            .build());
    if (response != null
        && response.getResponse() != null
        && response.getResponse().getBody() != null
        && response.getResponse().getBody().getItems() != null
    ) {
      List<Item> newInfos = response.getResponse().getBody().getItems().getItem();
      if (newInfos.isEmpty()) {
        throw new RuntimeException("No new index info found");
      }

      return response.getResponse().getBody().getItems().getItem();
    } else {
      throw new RuntimeException("OpenAPI Response value is NULL");
    }
  }
}
