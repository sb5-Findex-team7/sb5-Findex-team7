package com.codeit.team7.findex.exception.syncConfig;

import org.springframework.http.HttpStatus;

public class NoIndexInfoException extends SyncConfigException {

  public NoIndexInfoException(String message, String detail) {
    super(message, detail, HttpStatus.NOT_FOUND);
  }
}
