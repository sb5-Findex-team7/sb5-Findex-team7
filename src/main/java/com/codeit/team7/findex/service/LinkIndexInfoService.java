package com.codeit.team7.findex.service;

import com.codeit.team7.findex.dto.LinkIndexInfosDto;
import com.codeit.team7.findex.dto.SyncJobDto;
import java.util.List;

public interface LinkIndexInfoService {

  List<SyncJobDto> LinkIndexInfos(LinkIndexInfosDto linkIndexInfosDto);

}
