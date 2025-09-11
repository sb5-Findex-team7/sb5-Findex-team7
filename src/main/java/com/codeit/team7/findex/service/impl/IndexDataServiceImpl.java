package com.codeit.team7.findex.service.impl;

import com.codeit.team7.findex.domain.entity.IndexData;
import com.codeit.team7.findex.domain.entity.IndexInfo;
import com.codeit.team7.findex.domain.enums.SourceType;
import com.codeit.team7.findex.dto.IndexDataScrollRequest;
import com.codeit.team7.findex.dto.command.IndexDataDto;
import com.codeit.team7.findex.dto.request.IndexDataCreateRequest;
import com.codeit.team7.findex.dto.request.IndexDataUpdateRequest;
import com.codeit.team7.findex.dto.response.CursorPageResponseIndexDataDto;
import com.codeit.team7.findex.mapper.IndexDataMapper;
import com.codeit.team7.findex.repository.IndexDataQueryRepository;
import com.codeit.team7.findex.repository.IndexDataRepository;
import com.codeit.team7.findex.service.IndexDataService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IndexDataServiceImpl implements IndexDataService {

  private final IndexDataRepository indexDataRepository;
  private final IndexDataMapper indexDataMapper;
  private final IndexDataQueryRepository indexDataQueryRepository;


  @PersistenceContext
  private EntityManager em;

  @Transactional
  public IndexDataDto create(IndexDataCreateRequest request) {
    if(indexDataRepository.existsByIndexInfoIdAndBaseDate(request.getIndexInfoId(), request.getBaseDate())) {
      throw new IllegalArgumentException("이미 존재하는 값입니다.");
    }

    IndexData entity = indexDataMapper.toEntity(request);

    entity.setSourceType(SourceType.USER);
    entity.setIndexInfo(em.getReference(IndexInfo.class, request.getIndexInfoId()));

    IndexData saved = indexDataRepository.save(entity);
    return indexDataMapper.toDto(saved);
  }


  @Transactional
  public IndexDataDto update(Long id, IndexDataUpdateRequest request) {
    IndexData indexData = indexDataRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("아이디를 찾을 수 없습니다." + id));

    if (request.getMarketPrice() != null) {
      indexData.setMarketPrice(request.getMarketPrice());
    }
    if (request.getClosingPrice() != null) {
      indexData.setClosingPrice(request.getClosingPrice());
    }
    if (request.getHighPrice() != null) {
      indexData.setHighPrice(request.getHighPrice());
    }
    if (request.getLowPrice() != null) {
      indexData.setLowPrice(request.getLowPrice());
    }
    if (request.getVersus() != null) {
      indexData.setVersus(request.getVersus());
    }
    if (request.getFluctuationRate() != null) {
      indexData.setFluctuationRate(request.getFluctuationRate());
    }
    if (request.getTradingQuantity() != null) {
      indexData.setTradingQuantity(request.getTradingQuantity());
    }
    if (request.getMarketTotalAmount() != null) {
      indexData.setMarketTotalAmount(request.getMarketTotalAmount());
    }

    IndexData saved = indexDataRepository.save(indexData);
    return indexDataMapper.toDto(saved);
  }

  @Transactional
  public void deleteById(Long id) {
    indexDataRepository.deleteById(id);
  }

  @Transactional(readOnly = true)
  public CursorPageResponseIndexDataDto getIndexData(IndexDataScrollRequest request) {
    LocalDate startDate = request.getStartTime() != null ? request.getStartTime().toLocalDate() : null;
    LocalDate endDate = request.getEndTime() != null ? request.getEndTime().toLocalDate() : null;
    int size = request.pageSizeOrDefault();

    long totalElements;
    if (request.getIndexInfoId() == null) {
      totalElements = indexDataRepository.count();
    } else {
      totalElements = indexDataQueryRepository.countByIndexInfoId(request.getIndexInfoId());
    }

    List<IndexData> rows = indexDataQueryRepository.fetchPage(
        request.getIndexInfoId(),
        startDate,
        endDate,
        request.getIdAfter(),
        request.sortFieldOrDefault(),
        request.sortDirectionOrDefault(),
        size
    );

    boolean hasNext = rows.size() > size;
    if(hasNext) rows = rows.subList(0, size);

    List<IndexDataDto> content = rows.stream()
        .map(indexDataMapper::toDto)
        .toList();
    Long nextIdAfter = rows.isEmpty() ? null : rows.get(rows.size() -1).getId();

    LocalDateTime nextCursor = null;
    if (!rows.isEmpty() && rows.get(rows.size() -1).getCreatedAt() != null) {
      nextCursor = LocalDateTime.ofInstant(rows.get(rows.size() -1).getCreatedAt(), ZoneId.systemDefault());
    }

    return CursorPageResponseIndexDataDto.builder()
        .content(content)
        .nextCursor(nextCursor)
        .nextIdAfter(nextIdAfter)
        .size(size)
        .totalElements(totalElements)
        .hasNext(hasNext)
        .build();
  }
}
