package com.codeit.team7.findex.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true) // 불필요한 필드는 무시
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
    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate baseDate;
    @JsonProperty("idxNm")
    private String indexName;
    @JsonProperty("idxCsf")
    private String indexClassification;
    @JsonProperty("epyItmsCnt")
    private Integer ItemsCount;
    @JsonProperty("clpr")
    private BigDecimal closingPrice;
    @JsonProperty("vs")
    private BigDecimal versus;
    @JsonProperty("fltRt")
    private BigDecimal fluctuationRate;
    @JsonProperty("mkp")
    private BigDecimal marketPrice;
    @JsonProperty("hipr")
    private BigDecimal highPrice;
    @JsonProperty("lopr")
    private BigDecimal lowPrice;
    @JsonProperty("trqu")
    private Long tradingQuantity;
    @JsonProperty("trPrc")
    private Long tradingPrice;
    @JsonProperty("lstgMrktTotAmt")
    private Long marketTotalAmount;
    @JsonProperty("lsYrEdVsFltRg")
    private BigDecimal yearEndVersusFluctuationRange;
    @JsonProperty("yrWRcrdHgst")
    private BigDecimal yearRecordHighest;
    @JsonProperty("yrWRcrdHgstDt")
    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate yearRecordHighestDate;
    @JsonProperty("yrWRcrdLwst")
    private BigDecimal yearRecordLowest;
    @JsonProperty("yrWRcrdLwstDt")
    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate yearRecordLowestDate;
    @JsonProperty("basPntm")
    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate basePointInDate;
    @JsonProperty("basIdx")
    private BigDecimal baseIndex;
  }
}

