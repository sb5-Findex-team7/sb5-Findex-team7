package com.codeit.team7.findex.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

@Component
public class StockDateUtil {

  // 한국 주식시장 (KST 기준, 09:00 ~ 15:30 오픈)
  private static final LocalTime MARKET_OPEN = LocalTime.of(9, 0);
  private static final LocalTime MARKET_CLOSE = LocalTime.of(15, 30);
  private static final ZoneId KST = ZoneId.of("Asia/Seoul");

  /**
   * 오늘 기준 최신 데이터 날짜
   */
  public LocalDate getLatestDate() {
    LocalDate today = LocalDate.now(KST);

    if (isMarketOpen()) {
      // 장이 열려 있다면: 오늘은 미완성 → 어제 평일 반환
      return getPreviousWeekday(today);
    } else {
      // 장이 닫혀 있다면: 오늘이 평일이면 오늘 반환, 아니면 직전 평일 반환
      if (isWeekday(today)) {
        return today;
      }
      return getPreviousWeekday(today);
    }
  }

  /**
   * 주식 장이 열려 있는지 여부
   */
  private boolean isMarketOpen() {
    LocalDateTime now = LocalDateTime.now(KST);
    LocalTime time = now.toLocalTime();
    DayOfWeek day = now.getDayOfWeek();

    // 토/일은 휴장
    if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
      return false;
    }

    // 평일 장 시간대
    return !time.isBefore(MARKET_OPEN) && !time.isAfter(MARKET_CLOSE);
  }


  /**
   * 주어진 날짜 기준 직전 평일 반환
   */
  private LocalDate getPreviousWeekday(LocalDate date) {
    LocalDate prev = date.minusDays(1);
    while (!isWeekday(prev)) {
      prev = prev.minusDays(1);
    }
    return prev;
  }

  /**
   * 평일 여부 확인 (월~금)
   */
  private boolean isWeekday(LocalDate date) {
    DayOfWeek day = date.getDayOfWeek();
    return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
  }

}
