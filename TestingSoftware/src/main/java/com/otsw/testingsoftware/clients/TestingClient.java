package com.otsw.testingsoftware.clients;

import lombok.SneakyThrows;

import java.util.Random;

public class TestingClient extends Client {
  public TestingClient(int port) {
    super(port);
  }

  @SneakyThrows
  public void sendBytes(int count) {
    byte[] randomBytes = new byte[16];
    new Random().nextBytes(randomBytes);

    for(byte b : randomBytes) {
      sendByte(b);
    }
  }
}
