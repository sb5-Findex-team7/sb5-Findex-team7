package com.codeit.team7.findex.exception.handler;

import com.codeit.team7.findex.dto.response.ErrorResponse;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
    // TODO 나중에 예외 종류 별 세부처리
    return new ResponseEntity<>(ErrorResponse.builder()
                                             .timestamp(Instant.now())
                                             .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                             .message("Internal server error")
                                             .detail("temp")
                                             .build(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
