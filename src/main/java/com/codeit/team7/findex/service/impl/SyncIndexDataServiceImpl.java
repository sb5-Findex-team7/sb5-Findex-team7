package com.codeit.team7.findex.service.impl;

import com.codeit.team7.findex.service.SyncIndexDataService;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class SyncIndexDataServiceImpl implements SyncIndexDataService {

  public void sync(LocalDate targetDate) {
    // 2-1. enabled 된 지수 정보(DATA) 최신화
    // 2-2. 최신 데이터를 봤을 때 이미 최신화가 되었는지 확인
    // 2-3. syncJob에 반영
  }

}
