package com.codeit.team7.findex.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class StockMarketIndexResponse {

  @JsonProperty("response")
  private Response response;

  @Getter
  @Builder
  @ToString
  public static class Response {

    @JsonProperty("header")
    private Header header;

    @JsonProperty("body")
    private Body body;
  }

  @Getter
  @Builder
  @ToString
  public static class Header {

    @JsonProperty("resultCode")
    private String resultCode;

    @JsonProperty("resultMsg")
    private String resultMsg;
  }

  @Getter
  @Builder
  @ToString
  public static class Body {

    @JsonProperty("items")
    private Items items;

    @JsonProperty("numOfRows")
    private Integer numOfRows;

    @JsonProperty("pageNo")
    private Integer pageNo;

    @JsonProperty("totalCount")
    private Integer totalCount;
  }

  @Getter
  @Setter
  @Builder
  @ToString
  public static class Items {

    @JsonProperty("item")
    private List<Item> item;
  }

  @Getter
  @Setter
  @Builder
  @ToString
  public static class Item {

    @JsonProperty("basDt")
    private String baseDate;

    @JsonProperty("idxNm")
    private String indexName;

    @JsonProperty("clpr")
    private String closingPrice;

    @JsonProperty("vs")
    private String versus;

    @JsonProperty("fltRt")
    private String fluctuationRate;

    @JsonProperty("mkp")
    private String marketPrice;

    @JsonProperty("hipr")
    private String highPrice;

    @JsonProperty("lopr")
    private String lowPrice;

    @JsonProperty("trqu")
    private String tradingQuantity;

    @JsonProperty("trPrc")
    private String tradingPrice;

    @JsonProperty("lstgMrktTotAmt")
    private String marketTotalAmount;

    @JsonProperty("sourceType")
    private String sourceType;
  }
}

