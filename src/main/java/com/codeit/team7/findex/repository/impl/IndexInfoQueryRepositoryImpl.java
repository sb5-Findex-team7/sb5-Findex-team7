package com.codeit.team7.findex.repository.impl;

import com.codeit.team7.findex.domain.entity.IndexInfo;
import com.codeit.team7.findex.domain.entity.QIndexInfo;
import com.codeit.team7.findex.dto.PaginatedResult;
import com.codeit.team7.findex.repository.IndexInfoQueryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class IndexInfoQueryRepositoryImpl implements IndexInfoQueryRepository {

  private final JPAQueryFactory jpaQueryFactory;

  private static final QIndexInfo indexInfo = QIndexInfo.indexInfo;

  @Override
  public PaginatedResult<IndexInfo> search(
      String indexClassification, String indexName, Boolean favorite,
      String sortField, String sortDirection, String lastSortValue,
      Long lastId, int pageSize) {

    BooleanBuilder where = new BooleanBuilder();

    if (indexClassification != null && !indexClassification.isBlank()) {
      where.and(indexInfo.indexClassification.containsIgnoreCase(indexClassification));
    }
    if (indexName != null && !indexName.isBlank()) {
      where.and(indexInfo.indexName.containsIgnoreCase(indexName));
    }
    if (favorite != null) {
      where.and(indexInfo.favorite.eq(favorite));
    }

    String resolvedSortField =
        (sortField == null || sortField.isBlank()) ? "indexClassification" : sortField;
    boolean ascending = !"desc".equalsIgnoreCase(sortDirection);

    if (lastId != null) {
      switch (resolvedSortField) {
        case "indexName" -> {
          if (lastSortValue != null) {
            where.and(
                ascending
                    ? indexInfo.indexName.gt(lastSortValue)
                    .or(indexInfo.indexName.eq(lastSortValue).and(indexInfo.id.gt(lastId)))
                    : indexInfo.indexName.lt(lastSortValue)
                        .or(indexInfo.indexName.eq(lastSortValue).and(indexInfo.id.lt(lastId)))
            );
          } else {
            where.and(ascending ? indexInfo.id.gt(lastId) : indexInfo.id.lt(lastId));
          }
        }
        case "employedItemsCount" -> {
          Integer lastItemCount = null;
          try {
            if (lastSortValue != null) {
              lastItemCount = Integer.valueOf(lastSortValue);
            }
          } catch (NumberFormatException ignore) {
          }

          if (lastItemCount != null) {
            where.and(
                ascending
                    ? indexInfo.itemCount.gt(lastItemCount)
                    .or(indexInfo.itemCount.eq(lastItemCount).and(indexInfo.id.gt(lastId)))
                    : indexInfo.itemCount.lt(lastItemCount)
                        .or(indexInfo.itemCount.eq(lastItemCount).and(indexInfo.id.lt(lastId)))
            );
          } else {
            where.and(ascending ? indexInfo.id.gt(lastId) : indexInfo.id.lt(lastId));
          }
        }
        default -> {
          if (lastSortValue != null) {
            where.and(
                ascending
                    ? indexInfo.indexClassification.gt(lastSortValue)
                    .or(indexInfo.indexClassification.eq(lastSortValue)
                        .and(indexInfo.id.gt(lastId)))
                    : indexInfo.indexClassification.lt(lastSortValue)
                        .or(indexInfo.indexClassification.eq(lastSortValue)
                            .and(indexInfo.id.lt(lastId)))
            );
          } else {
            where.and(ascending ? indexInfo.id.gt(lastId) : indexInfo.id.lt(lastId));
          }
        }
      }
    }

    OrderSpecifier<?> mainOrder =
        switch (resolvedSortField) {
          case "indexName" -> ascending ? indexInfo.indexName.asc() : indexInfo.indexName.desc();
          case "employedItemsCount" ->
              ascending ? indexInfo.itemCount.asc() : indexInfo.itemCount.desc();
          default -> ascending ? indexInfo.indexClassification.asc()
              : indexInfo.indexClassification.desc();
        };

    OrderSpecifier<?> subOrder =
        ascending ? indexInfo.id.asc() : indexInfo.id.desc();

    int limit = Math.max(1, pageSize);

    List<IndexInfo> rows = jpaQueryFactory
        .selectFrom(indexInfo)
        .where(where)
        .orderBy(mainOrder, subOrder)
        .limit(limit + 1L)
        .fetch();

    boolean hasNext = rows.size() > limit;
    if (hasNext) {
      rows = rows.subList(0, limit);
    }

    Long total = jpaQueryFactory
        .select(indexInfo.count())
        .from(indexInfo)
        .where(where)
        .fetchOne();

    long totalElements = (total == null) ? 0L : total;

    return new PaginatedResult<>(rows, totalElements, hasNext);
  }
}
