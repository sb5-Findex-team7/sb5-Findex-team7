package com.codeit.team7.findex.mapper;

import com.codeit.team7.findex.domain.entity.IndexData;
import com.codeit.team7.findex.dto.command.IndexDataDto;
import com.codeit.team7.findex.dto.request.IndexDataCreateRequest;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IndexDataMapper {

  //  @Mapping(target = "id", ignore = true)
  @Mapping(target = "indexInfo", ignore = true)
  @Mapping(target = "sourceType", ignore = true)
  IndexData toEntity(IndexDataCreateRequest request);

  @Mapping(target = "indexInfoId", source = "indexInfo.id")
  IndexDataDto toDto(IndexData entity);

  List<IndexDataDto> toDtoList(List<IndexData> entities);
}