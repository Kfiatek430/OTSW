package com.kfiatek.otsw.clients;

import com.kfiatek.otsw.enums.TextTypes;
import lombok.SneakyThrows;
import java.util.EnumSet;
import java.util.Random;

public class TestingClient extends Client {
  private final int id;
  private final int count;
  private final int length;
  private final EnumSet<TextTypes> types;
  private final int frequencyMs;

  public TestingClient(int id, int port, int count, int length, EnumSet<TextTypes> types, int frequencyMs) {
    super(port);
    this.id = id;
    this.count = count;
    this.length = length;
    this.types = types;
    this.frequencyMs = frequencyMs;
  }

  @SneakyThrows
  public void run() {
    isConnected = performHandshake();
    if(isConnected) {
      new Thread(this::receiveMessages, "ClientReceiver." + port + '.' + id);
      new Thread(this::performTest, "ClientPerformer." + port + '.' + id);
    }
  }

  @SneakyThrows
  public void performTest() {
    for (int i = 0; i < count; i++) {
      sendText(length, types);
      Thread.sleep(frequencyMs);
    }
  }

  @SneakyThrows
  public void sendText(int length, EnumSet<TextTypes> types) {
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
    while(builder.length() < length) {
      int index = (int) (random.nextFloat() * characters.length());
      builder.append(characters.charAt(index));
    }

    sendText(builder.toString());
  }
}
