package com.kfiatek.otsw.clients;

import com.kfiatek.otsw.enums.TextTypes;
import lombok.SneakyThrows;
import java.util.EnumSet;
import java.util.Random;

public class TestingClient extends Client {
  public TestingClient(int port) {
    super(port);
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

    sendText(builder.toString());
  }

  public void sendText(int count) {
    sendText(count, EnumSet.of(TextTypes.LOWER_CASE));
  }

  public void sendText() {
    sendText(10);
  }
}
