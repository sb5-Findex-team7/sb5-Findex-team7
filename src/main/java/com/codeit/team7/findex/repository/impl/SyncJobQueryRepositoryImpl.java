package com.codeit.team7.findex.repository.impl;

import static com.codeit.team7.findex.domain.enums.SortedDirection.desc;
import static com.codeit.team7.findex.domain.enums.SyncJobSortedField.jobTime;

import com.codeit.team7.findex.domain.entity.QSyncJob;
import com.codeit.team7.findex.domain.entity.SyncJob;
import com.codeit.team7.findex.domain.enums.JobType;
import com.codeit.team7.findex.domain.enums.SortedDirection;
import com.codeit.team7.findex.domain.enums.SyncJobSortedField;
import com.codeit.team7.findex.dto.PaginatedResult;
import com.codeit.team7.findex.repository.SyncJobQueryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SyncJobQueryRepositoryImpl implements
    SyncJobQueryRepository {

  private final JPAQueryFactory queryFactory;
  private static final QSyncJob sj = QSyncJob.syncJob;

  @Override
  public PaginatedResult<SyncJob> searchSyncJob(
      JobType jobType,
      Long indexInfoId,
      LocalDate baseDateFrom,
      LocalDate baseDateTo,
      String worker,
      LocalDateTime jobTimeFrom,
      LocalDateTime jobTimeTo,
      Boolean status,
      Long idAfter,
      LocalDateTime cursor,
      SyncJobSortedField sortField,
      SortedDirection sortDirection,
      int size
  ) {

    // 1. where 조건 빌드
    BooleanBuilder where = new BooleanBuilder();

    if (jobType != null) {
      where.and(sj.jobType.eq(jobType.toString()));
    }
    if (indexInfoId != null) {
      where.and(sj.indexInfo.id.eq(indexInfoId));
    }
    if (baseDateFrom != null) {
      where.and(sj.targetDate.goe(baseDateFrom));
    }
    if (baseDateTo != null) {
      where.and(sj.targetDate.loe(baseDateTo));
    }
    if (worker != null && !worker.isBlank()) {
      where.and(sj.worker.eq(worker));
    }
    if (jobTimeFrom != null) {
      where.and(sj.jobTime.goe(jobTimeFrom.atZone(ZoneId.systemDefault()).toInstant()));
    }
    if (jobTimeTo != null) {
      where.and(sj.jobTime.loe(jobTimeTo.atZone(ZoneId.systemDefault()).toInstant()));
    }
    if (status != null) {
      where.and(sj.isCompleted.eq(status));
    }
    if (idAfter != null) {
      where.and(sj.id.gt(idAfter));
    }

    // 2. 페이지 네이션
    OrderSpecifier<?> order;
    // 2-1 정렬 조건 GET
    SyncJobSortedField sortedField = Optional.ofNullable(sortField)
        .orElse(SyncJobSortedField.id);

    SortedDirection direction = Optional.ofNullable(sortDirection)
        .orElse(desc);

    if (sortedField.equals(jobTime)) {
      order = direction.equals(desc) ? sj.jobTime.desc() : sj.jobTime.asc();
    } else if (sortedField == SyncJobSortedField.targetDate) {
      order = direction.equals(desc) ? sj.targetDate.desc() : sj.targetDate.asc();
    } else {
      order = direction.equals(desc) ? sj.jobTime.desc() : sj.jobTime.asc(); // default 는 jobTime
    }

    if (cursor != null) {
      Instant cursorInstant = cursor.atZone(ZoneId.systemDefault()).toInstant();
      LocalDate cursorDate = cursor.toLocalDate();
      if (sortedField.equals(jobTime)) {
        if (direction.equals(desc)) {
          where.and(sj.jobTime.lt(cursorInstant));
        } else {
          where.and(sj.jobTime.gt(cursorInstant));
        }
      } else if (sortedField == SyncJobSortedField.targetDate) {
        if (direction.equals(desc)) {
          where.and(sj.targetDate.lt(cursorDate));
        } else {
          where.and(sj.targetDate.gt(cursorDate));
        }
      } else {   // default 는 jobTime
        if (direction.equals(desc)) {
          where.and(sj.jobTime.lt(cursorInstant));
        } else {
          where.and(sj.jobTime.gt(cursorInstant));
        }
      }
    }

    List<SyncJob> contents = queryFactory.selectFrom(sj)
        .where(where)
        .join(sj.indexInfo).fetchJoin()
        .orderBy(order)
        .limit(size)
        .fetch();

    Long total = Optional.ofNullable(queryFactory
        .select(sj.id.countDistinct())
        .from(sj)
        .where(where)
        .fetchOne()).orElse(0L);

    return PaginatedResult.<SyncJob>builder()
        .content(contents)
        .totalElements(total)
        .hasNext(total > size)
        .build();

  }
}
