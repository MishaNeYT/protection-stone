package ru.mishaneyt.protectionstone.stone.exception;

public final class InvalidProtectionStone extends Exception {
  private final String message;

  public InvalidProtectionStone(String message) {
    super(message);
    this.message = message;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
