package com.heartsave.todaktodak_api.common.exception;

import com.heartsave.todaktodak_api.common.type.ErrorSpec;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseException extends RuntimeException {
  private final ErrorSpec errorSpec;

  @Override
  public String getMessage() {
    return errorSpec.getMessage();
  }
}
