package com.codeit.team7.findex.service.impl;

import com.codeit.team7.findex.domain.entity.IndexInfo;
import com.codeit.team7.findex.dto.command.IndexInfoDto;
import com.codeit.team7.findex.dto.command.IndexInfoSummaryDto;
import com.codeit.team7.findex.dto.request.IndexInfoCreateRequest;
import com.codeit.team7.findex.dto.request.IndexInfoUpdateRequest;
import com.codeit.team7.findex.repository.IndexInfoRepository;
import com.codeit.team7.findex.service.IndexInfoService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class IndexInfoServiceImpl implements IndexInfoService {

  private final IndexInfoRepository indexInfoRepository;

  @Transactional
  @Override
  public IndexInfoDto create(IndexInfoCreateRequest request) {
    IndexInfo indexInfo = request.toEntity();
    indexInfoRepository.save(indexInfo);
    return IndexInfoDto.fromEntity(indexInfo);
  }

  @Override
  public IndexInfoDto findById(Long id) {
    IndexInfo indexInfo = indexInfoRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("IndexInfo with id " + id + " not found"));
    return IndexInfoDto.fromEntity(indexInfo);
  }

  @Transactional
  @Override
  public IndexInfoDto update(Long id, IndexInfoUpdateRequest request) {
    IndexInfo indexInfo = indexInfoRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("IndexInfo with id " + id + " not found"));
    boolean newFavorite = request.favorite() != null ? request.favorite() : indexInfo.isFavorite();
    indexInfo.update(request.employedItemsCount(), request.basePointInTime(), request.baseIndex(),
        newFavorite);
    return IndexInfoDto.fromEntity(indexInfo);
  }

  @Override
  public void deleteById(Long id) {
    if (!indexInfoRepository.existsById(id)) {
      throw new NoSuchElementException("IndexInfo with id " + id + " not found");
    }
    // 자식 삭제되는거 생각해야할듯
    indexInfoRepository.deleteById(id);
  }

  @Override
  public List<IndexInfoSummaryDto> findAllSummaries() {
    List<IndexInfo> indexInfos = indexInfoRepository.findAll();
    return indexInfos.stream().map(IndexInfoSummaryDto::fromEntity).toList();
  }
}
