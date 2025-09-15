package com.codeit.team7.findex.util;

import com.codeit.team7.findex.domain.enums.PeriodType;
import com.codeit.team7.findex.dto.response.IndexChartDto;
import com.codeit.team7.findex.dto.response.IndexPerformanceRankDto;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CacheUtil {

  private static Map<String, IndexChartDto> chartDtoMap;
  private static Map<PeriodType, List<IndexPerformanceRankDto>> rankDtoMap;
  private static Map<PeriodType, List<IndexPerformanceRankDto.IndexPerformanceDto>> favoritePerformanceMap;

  public static IndexChartDto getChartDto(Long id, PeriodType periodType) {

    if (chartDtoMap == null) {
      chartDtoMap = new TreeMap<>();
    }

    return chartDtoMap.get(String.format("%s_%s", id, periodType));
  }

  public static void setChartDto(Long id, PeriodType periodType, IndexChartDto dto) {
    if (chartDtoMap == null) {
      chartDtoMap = new TreeMap<>();
    }
    chartDtoMap.put(String.format("%s_%s", id, periodType), dto);
  }

  public static List<IndexPerformanceRankDto> getRankDto(PeriodType periodType) {
    if (rankDtoMap == null) {
      rankDtoMap = new TreeMap<>();
    }
    return rankDtoMap.get(periodType);
  }

  public static void setRankDto(PeriodType periodType, List<IndexPerformanceRankDto> dto) {
    if (rankDtoMap == null) {
      rankDtoMap = new TreeMap<>();
    }
    rankDtoMap.put(periodType, dto);
  }

  public static List<IndexPerformanceRankDto.IndexPerformanceDto> getFavoritePerformance(
      PeriodType periodType) {
    if (favoritePerformanceMap == null) {
      favoritePerformanceMap = new TreeMap<>();
    }
    return favoritePerformanceMap.get(periodType);
  }

  public static void setFavoritePerformance(PeriodType periodType,
      List<IndexPerformanceRankDto.IndexPerformanceDto> dto) {
    if (favoritePerformanceMap == null) {
      favoritePerformanceMap = new TreeMap<>();
    }
    favoritePerformanceMap.put(periodType, dto);
  }

  public static void clearFavoritePerformance() {
    favoritePerformanceMap.clear();
  }

}
