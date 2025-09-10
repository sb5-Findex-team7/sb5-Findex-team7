package com.codeit.team7.findex.util;

import com.codeit.team7.findex.dto.request.StockMarketIndexRequest;
import com.codeit.team7.findex.dto.response.StockMarketIndexResponse;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OpenApiUtil {

  private final RestClient restClient;

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

    return restClient.get()
                     .uri(buildStockMarketIndexUrl(request))
                     .retrieve()
                     .body(StockMarketIndexResponse.class);
  }

  private URI buildStockMarketIndexUrl(StockMarketIndexRequest request) {

    UriComponentsBuilder builder = UriComponentsBuilder
        .fromUriString(baseUrl)
        .path(STOCK_MARKET_INDEX_URL)
        .queryParam("serviceKey", serviceKey)
        .queryParam("resultType", resultType);

    if (request == null) {
      return builder
          .queryParam("pageNo", DEFAULT_PAGE_NO)
          .queryParam("numOfRows", DEFAULT_NUM_OF_ROWS)
          .build()
          .toUri();
    }

    int pageNo =
        (request.getPageNo() != null) ? request.getPageNo() : DEFAULT_PAGE_NO;
    int numOfRows = (request.getNumOfRows() != null) ? request.getNumOfRows()
        : DEFAULT_NUM_OF_ROWS;

    builder.queryParam("pageNo", pageNo);
    builder.queryParam("numOfRows", numOfRows);

    if (request.getBasDt() != null) {
      builder.queryParam("basDt", request.getBasDt());
    }
    if (request.getBeginBasDt() != null) {
      builder.queryParam("beginBasDt", request.getBeginBasDt());
    }
    if (request.getEndBasDt() != null) {
      builder.queryParam("endBasDt", request.getEndBasDt());
    }
    if (request.getLikeBasDt() != null) {
      builder.queryParam("likeBasDt", request.getLikeBasDt());
    }
    if (request.getIdxNm() != null) {
      builder.queryParam("idxNm", request.getIdxNm());
    }
    if (request.getLikeIdxNm() != null) {
      builder.queryParam("likeIdxNm", request.getLikeIdxNm());
    }
    if (request.getBeginEpyItmsCnt() != null) {
      builder.queryParam("beginEpyItmsCnt", request.getBeginEpyItmsCnt());
    }
    if (request.getEndEpyItmsCnt() != null) {
      builder.queryParam("endEpyItmsCnt", request.getEndEpyItmsCnt());
    }
    if (request.getBeginFltRt() != null) {
      builder.queryParam("beginFltRt", request.getBeginFltRt());
    }
    if (request.getEndFltRt() != null) {
      builder.queryParam("endFltRt", request.getEndFltRt());
    }
    if (request.getBeginTrqu() != null) {
      builder.queryParam("beginTrqu", request.getBeginTrqu());
    }
    if (request.getEndTrqu() != null) {
      builder.queryParam("endTrqu", request.getEndTrqu());
    }
    if (request.getBeginLstgMrktTotAmt() != null) {
      builder.queryParam("beginLstgMrktTotAmt", request.getBeginLstgMrktTotAmt());
    }
    if (request.getEndLstgMrktTotAmt() != null) {
      builder.queryParam("endLstgMrktTotAmt", request.getEndLstgMrktTotAmt());
    }
    if (request.getBeginLsYrEdVsFltRg() != null) {
      builder.queryParam("beginLsYrEdVsFltRg", request.getBeginLsYrEdVsFltRg());
    }
    if (request.getEndLsYrEdVsFltRg() != null) {
      builder.queryParam("endLsYrEdVsFltRg", request.getEndLsYrEdVsFltRg());
    }
    if (request.getBeginLsYrEdVsFltRt() != null) {
      builder.queryParam("beginLsYrEdVsFltRt", request.getBeginLsYrEdVsFltRt());
    }
    if (request.getEndLsYrEdVsFltRt() != null) {
      builder.queryParam("endLsYrEdVsFltRt", request.getEndLsYrEdVsFltRt());
    }

    return builder.build()
                  .toUri();
  }
}
