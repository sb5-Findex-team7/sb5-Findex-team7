package com.codeit.team7.findex.util;

import com.codeit.team7.findex.dto.request.StockMarketIndexRequest;
import com.codeit.team7.findex.dto.response.StockMarketIndexResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class OpenApiUtil {

  @Autowired
  private RestTemplate restTemplate;

  // TODO config 정리
  private final String serviceKey = "4daecd572c3bf86f7016a54b0c7779d23ef08ae92ae037535312f3b45e846500";
  private final String resultType = "json";
  private final String pageNo = "1";
  private final String numOfRows = "50";

  public StockMarketIndexResponse fetchStockMarketIndex(StockMarketIndexRequest request) {
    String url = "https://apis.data.go.kr/1160100/service/GetMarketIndexInfoService/getStockMarketIndex?serviceKey=4daecd572c3bf86f7016a54b0c7779d23ef08ae92ae037535312f3b45e846500&resultType=json&pageNo=1&numOfRows=50";

    return restTemplate.getForObject(url, StockMarketIndexResponse.class);
  }
}
