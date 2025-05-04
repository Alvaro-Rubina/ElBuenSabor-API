package org.spdgrupo.elbuensaborapi.config.exception;

public class CyclicParentException extends RuntimeException {
  public CyclicParentException(String message) {
    super(message);
  }
}
