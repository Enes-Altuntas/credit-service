package org.colendi.domain.exception;

public class CreditDomainException extends DomainException{

  public CreditDomainException(String message) {
    super(message);
  }

  public CreditDomainException(String message, Throwable cause) {
    super(message, cause);
  }
}
