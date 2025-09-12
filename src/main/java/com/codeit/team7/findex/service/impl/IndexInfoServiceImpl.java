package com.codeit.team7.findex.service.impl;

import com.codeit.team7.findex.domain.entity.IndexInfo;
import com.codeit.team7.findex.dto.CursorPageResponseIndexInfoDto;
import com.codeit.team7.findex.dto.PaginatedResult;
import com.codeit.team7.findex.dto.command.IndexInfoDto;
import com.codeit.team7.findex.dto.request.IndexInfoCreateRequest;
import com.codeit.team7.findex.dto.request.IndexInfoUpdateRequest;
import com.codeit.team7.findex.dto.response.IndexInfoSummaryDto;
import com.codeit.team7.findex.repository.IndexInfoRepository;
import com.codeit.team7.findex.service.IndexInfoService;
import com.codeit.team7.findex.util.CacheUtil;
import jakarta.transaction.Transactional;
import java.util.Base64;
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
                                             .orElseThrow(() -> new NoSuchElementException(
                                                 "IndexInfo with id " + id + " not found"));
    return IndexInfoDto.fromEntity(indexInfo);
  }

  @Transactional
  @Override
  public IndexInfoDto update(Long id, IndexInfoUpdateRequest request) {
    IndexInfo indexInfo = indexInfoRepository.findById(id)
                                             .orElseThrow(() -> new NoSuchElementException(
                                                 "IndexInfo with id " + id + " not found"));
    boolean newFavorite = request.favorite() != null ? request.favorite() : indexInfo.getFavorite();
    if (indexInfo.getFavorite() != newFavorite) {
      CacheUtil.clearFavoritePerformance();
    }
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
    return indexInfos.stream()
                     .map(IndexInfoSummaryDto::fromEntity)
                     .toList();
  }

  @Override
  public CursorPageResponseIndexInfoDto search(String indexClassification, String indexName,
      Boolean favorite, String sortField, String sortDirection, Long idAfter, String cursor,
      int size) {

    String lastSortValue = null;
    Long lastId = idAfter;

    if (cursor != null && !cursor.isBlank()) {
      try {
        String decoded = new String(Base64.getDecoder()
                                          .decode(cursor));
        String[] parts = decoded.split("_");
        if (parts.length == 2) {
          lastSortValue = "null".equals(parts[0]) ? null : parts[0];
          lastId = Long.valueOf(parts[1]);
        }
      } catch (Exception ignore) {

      }
    }

    PaginatedResult<IndexInfo> result = indexInfoRepository.search(
        indexClassification, indexName, favorite, sortField,
        sortDirection, lastSortValue, lastId, size
    );

    List<IndexInfoDto> dtoList = result.getContent()
                                       .stream()
                                       .map(IndexInfoDto::fromEntity)
                                       .toList();

    String nextCursor = null;
    Long nextIdAfter = null;
    if (!dtoList.isEmpty() && result.getHasNext()) {
      IndexInfo last = result.getContent()
                             .get(result.getContent()
                                        .size() - 1);
      String sortValue = switch (sortField == null ? "indexClassification" : sortField) {
        case "indexName" -> last.getIndexName();
        case "employedItemsCount" -> last.getItemCount() == null ? null : last.getItemCount()
                                                                              .toString();
        default -> last.getIndexClassification();
      };
      nextCursor = Base64.getEncoder()
                         .encodeToString(
                             ((sortValue == null ? "null" : sortValue) + "_"
                              + last.getId()).getBytes());
      nextIdAfter = last.getId();
    }

    return new CursorPageResponseIndexInfoDto(
        dtoList,
        nextCursor,
        nextIdAfter,
        dtoList.size(),
        result.getTotalElements(),
        result.getHasNext()
    );
  }
}
