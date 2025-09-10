package com.codeit.team7.findex.mapper;

import com.codeit.team7.findex.domain.entity.IndexData;
import com.codeit.team7.findex.dto.response.IndexDataDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IndexDataMapper {

  @Mapping(target = "indexInfoId", source = "indexInfo.id")
  IndexDataDto toDto(IndexData entity);

  List<IndexDataDto> toDtoList(List<IndexData> entities);
}