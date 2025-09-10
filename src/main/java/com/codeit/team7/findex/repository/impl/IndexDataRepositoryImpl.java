package com.codeit.team7.findex.repository.impl;

import static com.codeit.team7.findex.domain.enums.IndexDataSortDirection.asc;

import com.codeit.team7.findex.domain.entity.IndexData;
import com.codeit.team7.findex.domain.entity.QIndexData;
import com.codeit.team7.findex.domain.enums.IndexDataSortDirection;
import com.codeit.team7.findex.domain.enums.IndexDataSortField;
import com.codeit.team7.findex.repository.IndexDataQueryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IndexDataRepositoryImpl implements IndexDataQueryRepository {

  private final JPAQueryFactory qf;
  private static final QIndexData d = QIndexData.indexData;

  @Override
  public List<IndexData> fetchPage(
      Long indexInfoId,
      LocalDate startDate,
      LocalDate endDate,
      Long idAfter,
      IndexDataSortField sortField,
      IndexDataSortDirection sortDirection,
      int size
  ) {
    int pageSize = Math.max(1, Math.min(size, 100));

    BooleanBuilder where = new BooleanBuilder();
    if (indexInfoId != null) {
      where.and(d.indexInfo.id.eq(indexInfoId));
    }
    if (startDate != null) {
      where.and(d.baseDate.goe(startDate));
    }
    if (endDate != null) {
      where.and(d.baseDate.loe(endDate));
    }
    IndexData anchor = null;
    if (idAfter != null) {
      anchor = qf.selectFrom(d).where(d.id.eq(idAfter)).fetchOne();
      if(anchor == null) return Collections.emptyList();
      if(indexInfoId != null && !indexInfoId.equals(anchor.getIndexInfo().getId())) return Collections.emptyList();
      if(startDate != null && anchor.getBaseDate().isBefore(startDate)) return Collections.emptyList();
      if(endDate != null && anchor.getBaseDate().isAfter(endDate)) return Collections.emptyList();

      BooleanExpression cursor = switch (sortField) {
        case baseDate -> dateCursor(d.baseDate, anchor.getBaseDate(), d.id, idAfter, sortDirection);
        case marketPrice -> numberCursor(d.marketPrice, anchor.getMarketPrice(), d.id, idAfter, sortDirection);
        case closingPrice -> numberCursor(d.closingPrice, anchor.getClosingPrice(), d.id, idAfter, sortDirection);
        case highPrice -> numberCursor(d.highPrice, anchor.getHighPrice(), d.id, idAfter, sortDirection);
        case lowPrice -> numberCursor(d.lowPrice, anchor.getLowPrice(), d.id, idAfter, sortDirection);
        case versus -> numberCursor(d.versus, anchor.getVersus(), d.id, idAfter, sortDirection);
        case fluctuationRate -> numberCursor(d.fluctuationRate, anchor.getFluctuationRate(), d.id, idAfter, sortDirection);
        case tradingQuantity -> numberCursor(d.tradingQuantity, anchor.getTradingQuantity(), d.id, idAfter, sortDirection);
        case tradingPrice -> numberCursor(d.tradingPrice, anchor.getTradingPrice(), d.id, idAfter, sortDirection);
        case marketTotalAmount -> numberCursor(d.marketTotalAmount, anchor.getMarketTotalAmount(), d.id, idAfter, sortDirection);
      };
      where.and(cursor);
    }
    OrderSpecifier<?> primary = switch (sortField) {
      case baseDate -> sortDirection == asc ? d.baseDate.asc() : d.baseDate.desc();
      case marketPrice -> sortDirection == asc ? d.marketPrice.asc() : d.marketPrice.desc();
      case closingPrice -> sortDirection == asc ? d.closingPrice.asc() : d.closingPrice.desc();
      case highPrice -> sortDirection == asc ? d.highPrice.asc() : d.highPrice.desc();
      case lowPrice -> sortDirection == asc ? d.lowPrice.asc() : d.lowPrice.desc();
      case versus -> sortDirection == asc ? d.versus.asc() : d.versus.desc();
      case fluctuationRate -> sortDirection == asc ? d.fluctuationRate.asc() : d.fluctuationRate.desc();
      case tradingQuantity -> sortDirection == asc ? d.tradingQuantity.asc() : d.tradingQuantity.desc();
      case tradingPrice -> sortDirection == asc ? d.tradingPrice.asc() : d.tradingPrice.desc();
      case marketTotalAmount -> sortDirection == asc ? d.marketTotalAmount.asc() : d.marketTotalAmount.desc();
    };
    OrderSpecifier<Long> tie = (sortDirection == asc) ? d.id.asc() : d.id.desc();
    return qf.selectFrom(d)
        .where(where)
        .orderBy(primary, tie)
        .limit(pageSize + 1L)
        .fetch();
  }

  @Override
  public Long countByIndexInfoId(Long indexInfoId) {
    QIndexData indexData = QIndexData.indexData;

    return qf
        .select(indexData.count())
        .from(indexData)
        .where(indexData.indexInfo.id.eq(indexInfoId))
        .fetchOne();
  }

  private BooleanExpression dateCursor(
      DatePath<LocalDate> sortExpr,
      LocalDate anchorSort,
      NumberPath<Long> idExpr,
      Long anchorId,
      IndexDataSortDirection dir
  ) {
    BooleanExpression tie = (dir == asc) ? idExpr.gt(anchorId) : idExpr.lt(anchorId);
    if (anchorSort == null) return tie;

    if (dir == asc) {
      return sortExpr.after(anchorSort).or(sortExpr.eq(anchorSort).and(tie));
    } else {
      return sortExpr.before(anchorSort).or(sortExpr.eq(anchorSort).and(tie));
    }
  }

  private <N extends Number & Comparable<N>> BooleanExpression numberCursor(
      com.querydsl.core.types.dsl.NumberExpression<N> sortExpr,
      N anchorSort,
      NumberPath<Long> idExpr,
      Long anchorId,
      IndexDataSortDirection dir
  ) {
    BooleanExpression tie = (dir == asc) ? idExpr.gt(anchorId) : idExpr.lt(anchorId);
    if (anchorSort == null) return tie;

    if (dir == asc) {
      return sortExpr.gt(anchorSort).or(sortExpr.eq(anchorSort).and(tie));
    } else {
      return sortExpr.lt(anchorSort).or(sortExpr.eq(anchorSort).and(tie));
    }
  }
}
