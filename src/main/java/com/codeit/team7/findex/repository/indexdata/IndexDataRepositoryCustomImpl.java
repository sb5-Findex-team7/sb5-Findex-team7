package com.codeit.team7.findex.repository.indexdata;

import com.codeit.team7.findex.domain.entity.IndexData;
import com.codeit.team7.findex.domain.entity.QIndexData;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class IndexDataRepositoryCustomImpl implements IndexDataRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<IndexData> findByCursorAndFilters(
      Long indexInfoId,
      LocalDate startDate,
      LocalDate endDate,
      LocalDate cursorDate,
      Long idAfter,
      String sortDirection,
      int pageSizePlusOne) {

    QIndexData d = QIndexData.indexData;
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

    boolean asc = "asc".equalsIgnoreCase(sortDirection);

    if (cursorDate != null) {
      if (idAfter != null) {
        if (asc) {
          where.and(
              d.baseDate.gt(cursorDate)
                        .or(d.baseDate.eq(cursorDate).and(d.id.gt(idAfter)))
          );
        } else {
          where.and(
              d.baseDate.lt(cursorDate)
                        .or(d.baseDate.eq(cursorDate).and(d.id.lt(idAfter)))
          );
        }
      } else {
        where.and(asc ? d.baseDate.gt(cursorDate) : d.baseDate.lt(cursorDate));
      }
    } else if (idAfter != null) {
      where.and(asc ? d.id.gt(idAfter) : d.id.lt(idAfter));
    }

    OrderSpecifier<?> primary = asc ? d.baseDate.asc() : d.baseDate.desc();
    OrderSpecifier<?> secondary = asc ? d.id.asc() : d.id.desc();

    return queryFactory.selectFrom(d)
                       .where(where)
                       .orderBy(primary, secondary)
                       .limit(pageSizePlusOne)
                       .fetch();
  }
}