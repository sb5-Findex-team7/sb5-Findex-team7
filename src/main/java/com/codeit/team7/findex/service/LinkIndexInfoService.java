package com.codeit.team7.findex.service;

import com.codeit.team7.findex.dto.GetNewIndexInfosResult;
import com.codeit.team7.findex.dto.SyncJobDto;
import com.codeit.team7.findex.dto.response.StockMarketIndexResponse.Item;
import java.util.List;

public interface LinkIndexInfoService {

  List<SyncJobDto> LinkIndexInfos(GetNewIndexInfosResult getNewIndexInfosResult);

}
