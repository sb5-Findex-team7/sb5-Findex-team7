package com.codeit.team7.findex.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class PatchSyncRequest {

  @NotNull
  Boolean enabled;
}
