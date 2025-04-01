package com.kfiatek.otsw.testingsoftware;

import com.kfiatek.otsw.clients.TestingClient;
import com.kfiatek.otsw.enums.TextTypes;
import lombok.SneakyThrows;

import java.util.EnumSet;

public class Main {
  @SneakyThrows
  public static void main(String[] args) {
    TestingClient client = new TestingClient(8000);
    client.waitForConnection();
    client.sendBytes(40);
    client.sendText(20, EnumSet.of(TextTypes.LOWER_CASE, TextTypes.UPPER_CASE));
  }
}