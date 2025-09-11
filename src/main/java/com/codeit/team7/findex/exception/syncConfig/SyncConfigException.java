package com.codeit.team7.findex.exception.syncConfig;

import java.time.Instant;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class SyncConfigException extends RuntimeException {

  private final Instant timestamp;
  private final String message;
  private final HttpStatus status;
  private final String detail;

  public SyncConfigException(String message, String detail, HttpStatus status) {
    super(message);
    this.timestamp = Instant.now();
    this.status = status;
    this.message = message;
    this.detail = detail;
  }
}
