package com.codeit.team7.findex.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class PatchSyncConfigCommand {

  Long indexInfoId;
  Boolean enabled;

}
