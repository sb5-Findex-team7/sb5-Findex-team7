package com.codeit.team7.findex.util;

import com.codeit.team7.findex.dto.request.StockMarketIndexRequest;
import com.codeit.team7.findex.dto.response.StockMarketIndexResponse;
import java.lang.reflect.Field;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class OpenApiUtil {

  @Autowired
  private final RestTemplate restTemplate;

  @Value("${spring.openapi.service_key}")
  private String serviceKey;
  @Value("${spring.openapi.result_type}")
  private String resultType;
  @Value("${spring.openapi.base_url}")
  private String baseUrl;

  private static final String STOCK_MARKET_INDEX_URL = "/1160100/service/GetMarketIndexInfoService/getStockMarketIndex";
  private static final int DEFAULT_PAGE_NO = 1;
  private static final int DEFAULT_NUM_OF_ROWS = 50;

  public StockMarketIndexResponse fetchStockMarketIndex(StockMarketIndexRequest request) {

    String url = buildStockMarketIndexUrl(request);

    return restTemplate.getForObject(url, StockMarketIndexResponse.class);
  }

  private String buildStockMarketIndexUrl(StockMarketIndexRequest request) {

    StringBuilder sb = new StringBuilder();

    sb.append(baseUrl);
    sb.append(STOCK_MARKET_INDEX_URL);
    sb.append(
        String.format("?serviceKey=%s&resultType=%s", serviceKey, resultType));

    if (request == null) {
      return sb.append(String.format("&pageNo=%d&numOfRows=%d", DEFAULT_PAGE_NO,
              DEFAULT_NUM_OF_ROWS))
          .toString();
    }

    Field[] fields = StockMarketIndexRequest.class.getDeclaredFields();
    for (Field field : fields) {
      try {

        field.setAccessible(true);
        Object value = field.get(request);

        if (field.getName()
            .equals("pageNo") && value == null) {
          sb.append("&")
              .append(field.getName())
              .append("=")
              .append(DEFAULT_PAGE_NO);
        } else if (field.getName()
            .equals("numOfRows") && value == null) {
          sb.append("&")
              .append(field.getName())
              .append("=")
              .append(DEFAULT_NUM_OF_ROWS);
        } else if (value != null) {
          sb.append("&")
              .append(field.getName())
              .append("=")
              .append(value);
        }
      } catch (IllegalAccessException e) {
        // TODO 필요하면 예외처리
      }
    }

    return sb.toString();
  }
}
