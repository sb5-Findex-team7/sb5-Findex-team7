package com.codeit.team7.findex.exception.sync;

import java.time.Instant;
import lombok.Getter;

@Getter
public abstract class SyncException extends RuntimeException {

  // todo abstract를 하는 것이 맞는가?
  private final Instant timestamp;
  private final String message;
  private final String detail;

  public SyncException(String message, String detail) {
    super(message);
    this.timestamp = Instant.now();
    this.message = message;
    this.detail = detail;
  }
}
