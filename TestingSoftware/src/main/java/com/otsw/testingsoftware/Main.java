package com.otsw.testingsoftware;

import com.otsw.testingsoftware.clients.TestingClient;
import lombok.SneakyThrows;

public class Main {
  @SneakyThrows
  public static void main(String[] args) {
    TestingClient client = new TestingClient(8000);
    client.waitForConnection();
    client.sendBytes(40);
 }
}