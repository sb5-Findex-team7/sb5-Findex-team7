package com.codeit.team7.findex.service;

import com.codeit.team7.findex.dto.GetNewIndexDataResult;
import com.codeit.team7.findex.dto.GetNewIndexInfosResult;
import com.codeit.team7.findex.dto.command.GetNewIndexDataCommand;

public interface OpenApiService {

  GetNewIndexInfosResult getNewIndexInfos();

  GetNewIndexDataResult getNewIndexData(GetNewIndexDataCommand command);
}
