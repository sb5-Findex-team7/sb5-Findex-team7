package com.codeit.team7.findex.dto.request;
import lombok.*;

@Getter
@Builder
public class StockMarketIndexRequest {

  private Integer pageNo;
  private Integer numOfRows;
  private String basDt;
  private String beginBasDt;
  private String endBasDt;
  private String likeBasDt;
  private String idxNm;
  private String likeIdxNm;
  private String beginEpyItmsCnt;
  private String endEpyItmsCnt;
  private String beginFltRt;
  private String endFltRt;
  private String beginTrqu;
  private String endTrqu;
  private String beginTrPrc;
  private String endTrPrc;
  private String beginLstgMrktTotAmt;
  private String endLstgMrktTotAmt;
  private String beginLsYrEdVsFltRg;
  private String endLsYrEdVsFltRg;
  private String beginLsYrEdVsFltRt;
  private String endLsYrEdVsFltRt;
}
