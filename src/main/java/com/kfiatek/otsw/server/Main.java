package com.kfiatek.otsw.server;

import lombok.SneakyThrows;

public class Main {
  @SneakyThrows
  public static void main(String[] args) {
    new Server(8000);
  }
}