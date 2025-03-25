package com.otsw.testingsoftware.clients;

import lombok.SneakyThrows;

import java.util.Base64;
import java.util.Random;

public class TestingClient extends Client {
  public TestingClient(int port) {
    super(port);
  }

  @SneakyThrows
  public void sendBytes(int count) {
    if(isConnected) {
      logger.warn("[!] Client is not connected yet!");
      return;
    }

    byte[] randomBytes = new byte[count];
    new Random().nextBytes(randomBytes);

    String encoded = Base64.getEncoder().encodeToString(randomBytes);
    writer.write(encoded + "\n");
    writer.flush();

    logger.info("[+] Sent {} bytes as Base64: {}", count, encoded);
  }
}
