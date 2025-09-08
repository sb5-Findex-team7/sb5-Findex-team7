package com.codeit.team7.findex.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auto-sync-configs")
public class SyncConfigController {

  @GetMapping
  public ResponseEntity<String> getSyncConfigs() {
    return ResponseEntity.ok("Sync configs");
  }

}
