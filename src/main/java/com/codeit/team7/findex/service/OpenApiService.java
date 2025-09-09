package com.codeit.team7.findex.service;

import com.codeit.team7.findex.dto.response.StockMarketIndexResponse.Item;
import java.util.List;

public interface OpenApiService {

  List<Item> GetNewIndexInfos();
}
