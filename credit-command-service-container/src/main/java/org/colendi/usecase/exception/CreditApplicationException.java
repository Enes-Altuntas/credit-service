package org.colendi.usecase.exception;

public class CreditApplicationException extends RuntimeException {

  public CreditApplicationException(String message) {
    super(message);
  }

  public CreditApplicationException(String message, Throwable cause) {
    super(message, cause);
  }
}
