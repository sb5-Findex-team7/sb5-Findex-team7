package com.codeit.team7.findex.dto;

import com.codeit.team7.findex.dto.command.IndexInfoDto;
import java.util.List;

public record CursorPageResponseIndexInfoDto(
    List<IndexInfoDto> content,
    String nextCursor,
    Long nextIdAfter,
    int size,
    Long totalElements,
    boolean hasNext
) {

}
