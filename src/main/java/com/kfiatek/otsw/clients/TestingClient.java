package com.kfiatek.otsw.clients;

import com.kfiatek.otsw.enums.TextTypes;
import lombok.Getter;
import lombok.SneakyThrows;
import java.util.EnumSet;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class TestingClient extends Client {
  private final int id;
  private final int count;
  private final int length;
  private final EnumSet<TextTypes> types;
  private final int frequencyMs;
  private final int timeoutMs;
  @Getter
  private int successCount = 0;
  @Getter
  private int failureCount = 0;
  private final Map<String, Long> sentMessagesTimeouts = new ConcurrentHashMap<>();
  private final CountDownLatch doneSignal;

  public TestingClient(int id, int port, int count, int length, EnumSet<TextTypes> types, int frequencyMs, int timeoutMs, CountDownLatch doneSignal) {
    super(port);
    this.id = id;
    this.count = count;
    this.length = length;
    this.types = types;
    this.frequencyMs = frequencyMs;
    this.timeoutMs = timeoutMs;
    this.doneSignal = doneSignal;
  }

  @SneakyThrows
  @Override
  public void run() {
    isConnected = performHandshake();
    if(isConnected) {
      new Thread(this::performTest, "ClientPerformer." + port + '.' + id).start();
      new Thread(this::receiveMessages, "ClientReceiver." + port + '.' + id).start();
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

  @SneakyThrows
  @Override
  public void sendText(String msg) {
    writer.write(msg + '\n');
    writer.flush();
    sentMessagesTimeouts.put(msg, System.currentTimeMillis());
  }

  @SneakyThrows
  @Override
  protected void receiveMessages() {
    String message = "";
    int receivedMessagesCounter = 0;
    while(shouldContinue) {
      if((message = reader.readLine()) != null) {
        Long sentTime = sentMessagesTimeouts.remove(message);
        if(sentTime != null) {
          receivedMessagesCounter++;
          long latency = System.currentTimeMillis() - sentTime;
          if (latency <= timeoutMs) {
            successCount++;
            logger.info("[{}] SUCCESS Received echo in {} ms: {}", id, latency, message);
          } else {
            failureCount++;
            logger.info("[{}] FAILED Received echo in {} ms: {}", id, latency, message);
          }

          if(receivedMessagesCounter == count) {
            closeClient();
          }
        }
      }
    }

    doneSignal.countDown();
  }


}
