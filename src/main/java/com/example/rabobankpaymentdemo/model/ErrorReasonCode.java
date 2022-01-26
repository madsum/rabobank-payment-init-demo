package com.example.rabobankpaymentdemo.model;

/**
 * Gets or Sets ErrorReasonCode
 */
public enum ErrorReasonCode {
  UNKNOWN_CERTIFICATE("UNKNOWN_CERTIFICATE"),
  INVALID_SIGNATURE("INVALID_SIGNATURE"),
  INVALID_REQUEST("INVALID_REQUEST"),
  LIMIT_EXCEEDED("LIMIT_EXCEEDED"),
  GENERAL_ERROR("GENERAL_ERROR");

  private String value;

  ErrorReasonCode(String value) {
    this.value = value;
  }

  public String toString() {
    return String.valueOf(value);
  }

}
