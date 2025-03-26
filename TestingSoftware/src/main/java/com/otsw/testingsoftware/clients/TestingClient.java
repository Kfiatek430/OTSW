package com.otsw.testingsoftware.clients;

import com.otsw.testingsoftware.enums.TextTypes;
import lombok.SneakyThrows;
import java.util.Base64;
import java.util.EnumSet;
import java.util.Random;

public class TestingClient extends Client {
  public TestingClient(int port) {
    super(port);
  }

  @SneakyThrows
  public void sendBytes(int count) {
    if(!isConnected) {
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

  public void sendBytes() {
    sendBytes(10);
  }

  @SneakyThrows
  public void sendText(int count, EnumSet<TextTypes> types) {
    if(!isConnected) {
      logger.warn("[!] Client is not connected yet!");
      return;
    }

    StringBuilder characters = new StringBuilder();
    for(TextTypes type : types) {
      characters.append(type.toString());
    }

    StringBuilder builder = new StringBuilder();
    Random random = new Random();
    while(builder.length() < count) {
      int index = (int) (random.nextFloat() * characters.length());
      builder.append(characters.charAt(index));
    }

    writer.write(builder.toString() + '\n');
    writer.flush();
  }

  public void sendText(int count) {
    sendText(count, EnumSet.of(TextTypes.LOWER_CASE));
  }

  public void sendText() {
    sendText(10);
  }
}
