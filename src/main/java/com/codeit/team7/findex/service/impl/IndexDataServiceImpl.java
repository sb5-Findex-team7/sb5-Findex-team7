package com.codeit.team7.findex.service.impl;

import com.codeit.team7.findex.domain.entity.IndexData;
import com.codeit.team7.findex.domain.entity.IndexInfo;
import com.codeit.team7.findex.domain.enums.SourceType;
import com.codeit.team7.findex.dto.command.IndexDataDto;
import com.codeit.team7.findex.dto.request.IndexDataCreateRequest;
import com.codeit.team7.findex.dto.request.IndexDataUpdateRequest;
import com.codeit.team7.findex.mapper.IndexDataMapper;
import com.codeit.team7.findex.repository.IndexDataRepository;
import com.codeit.team7.findex.service.IndexDataService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IndexDataServiceImpl implements IndexDataService {

  private final IndexDataRepository indexDataRepository;
  private final IndexDataMapper indexDataMapper;


  @PersistenceContext
  private EntityManager em;

  @Transactional
  public IndexDataDto create(IndexDataCreateRequest request, SourceType type) {
    if(indexDataRepository.existsByIndexInfoIdAndBaseDate(request.getIndexInfoId(), request.getBaseDate())) {
      throw new IllegalArgumentException("이미 존재하는 값입니다.");
    }
    IndexData e = indexDataMapper.toEntity(request);
    e.setSourceType(type);
    e.setIndexInfo(em.find(IndexInfo.class, request.getIndexInfoId()));

    IndexData entity = indexDataMapper.toEntity(request);
    entity.setIndexInfo(em.getReference(IndexInfo.class, request.getIndexInfoId()));
    IndexData saved = indexDataRepository.save(entity);
    return indexDataMapper.toDto(saved);
  }

  @Transactional(readOnly = true)
  public IndexDataDto findByIndexInfoId(Long indexInfoId) {
      IndexData indexData = indexDataRepository.findById(indexInfoId)
          .orElseThrow(() -> new NoSuchElementException("아이디를 찾을 수 없습니다." + indexInfoId));
      return indexDataMapper.toDto(indexData);

  }

  @Transactional
  public IndexDataDto update(Long id, IndexDataUpdateRequest request) {
    IndexData indexData = indexDataRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("아이디를 찾을 수 없습니다." + id));

    indexData.setMarketPrice(request.getMarketPrice());
    indexData.setClosingPrice(request.getClosingPrice());
    indexData.setHighPrice(request.getHighPrice());
    indexData.setLowPrice(request.getLowPrice());
    indexData.setVersus(request.getVersus());
    indexData.setFluctuationRate(request.getFluctuationRate());
    indexData.setTradingQuantity(request.getTradingQuantity());
    indexData.setTradingPrice(request.getTradingPrice());
    indexData.setMarketTotalAmount(request.getMarketTotalAmount());

    IndexData saved = indexDataRepository.save(indexData);
    return indexDataMapper.toDto(saved);
  }

  @Transactional
  public void deleteById(Long id) {
    indexDataRepository.deleteById(id);
  }
}
