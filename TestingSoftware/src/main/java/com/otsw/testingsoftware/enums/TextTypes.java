package com.otsw.testingsoftware.enums;

public enum TextTypes {
  LOWER_CASE("abcdefghijklmnoprstuwxyz"),
  UPPER_CASE("ABCDEFGHIJKLMNOPRSTUWXYZ"),
  DIGITS("0123456789"),
  SPECIAL_CHARACTERS("!@#$%^&*()_+-=[]{};':',./<>?~`");

  private final String characters;
  TextTypes(String characters) {
    this.characters = characters;
  }

  @Override
  public String toString() {
    return characters;
  }
}