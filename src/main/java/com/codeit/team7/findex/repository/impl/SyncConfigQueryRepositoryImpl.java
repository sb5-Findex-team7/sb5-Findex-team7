package com.codeit.team7.findex.repository.impl;

import static com.codeit.team7.findex.domain.enums.SortedDirection.desc;

import com.codeit.team7.findex.domain.entity.IndexInfo;
import com.codeit.team7.findex.domain.entity.QIndexInfo;
import com.codeit.team7.findex.domain.enums.SortedDirection;
import com.codeit.team7.findex.domain.enums.SyncConfigSoredField;
import com.codeit.team7.findex.dto.PaginatedResult;
import com.codeit.team7.findex.repository.SyncConfigQueryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SyncConfigQueryRepositoryImpl implements SyncConfigQueryRepository {

  private final JPAQueryFactory queryFactory;
  private static final QIndexInfo ii = QIndexInfo.indexInfo;

  @Override
  public PaginatedResult<IndexInfo> searchSyncConfig(
      Long IndexInfoId,
      Boolean enabled,
      Long idAfter,
      String cursor,
      SyncConfigSoredField sortField,
      SortedDirection sortDirection,
      int size
  ) {

    // 1. where 조건 빌드
    BooleanBuilder where = new BooleanBuilder();

    if (IndexInfoId != null) {
      where.and(ii.id.eq(IndexInfoId));
    }
    if (enabled != null) {
      where.and(ii.enabled.eq(enabled));
    }
    if (idAfter != null) {
      where.and(ii.id.gt(idAfter));
    }

    // 2. 페이지네이션
    OrderSpecifier<?> order;
    // 2-1 정렬 조건 GET
    SyncConfigSoredField soredField = Optional.ofNullable(sortField)
        .orElse(SyncConfigSoredField.indexName);

    SortedDirection sortedDirection = Optional.ofNullable(sortDirection)
        .orElse(desc);

    if (soredField == SyncConfigSoredField.indexName) {
      order = sortedDirection == desc ? ii.indexName.desc() : ii.indexName.asc();
    } else { // enabled
      order = sortedDirection == desc ? ii.enabled.desc() : ii.enabled.asc();
    }

    // cursor조건 처리

    if (cursor != null) {
      if (soredField == SyncConfigSoredField.indexName) {
        if (sortedDirection == desc) {
          where.and(ii.indexName.lt(cursor));
        } else {
          where.and(ii.indexName.gt(cursor));
        }
      } else { // enabled
        Boolean cursorBool = Boolean.parseBoolean(cursor);
        if (sortedDirection == desc) {
          where.and(ii.enabled.lt(cursorBool));
        } else {
          where.and(ii.enabled.gt(cursorBool));
        }
      }
    }
    if (cursor != null) {
      if (soredField == SyncConfigSoredField.indexName) {
        where.and(sortedDirection == desc ? ii.indexName.lt(cursor) : ii.indexName.gt(cursor));
      } else { // enabled
        Boolean cursorBool = Boolean.parseBoolean(cursor);
        where.and(sortedDirection == desc ? ii.enabled.lt(cursorBool) : ii.enabled.gt(cursorBool));
      }

    }

    List<IndexInfo> contents = queryFactory.selectFrom(ii)
        .where(where)
        .orderBy(order)
        .limit(size)
        .fetch();

    Long total = Optional.ofNullable(queryFactory
        .select(ii.count())
        .from(ii)
        .where(where)
        .fetchOne()).orElse(0L);
    
    return PaginatedResult.<IndexInfo>builder()
        .content(contents)
        .totalElements(total)
        .hasNext(total > size)
        .build();
  }

}
