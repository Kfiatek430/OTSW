package com.kfiatek.otsw.clients;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ConsoleClient extends Client {
  BufferedReader consoleReader;
  private Thread sender;

  public ConsoleClient(int port) {
    super(port);
  }

  @Override
  protected void setupBuffered() {
    consoleReader = new BufferedReader(new InputStreamReader(System.in));
    super.setupBuffered();
  }

  @Override
  public void run() {
    sender = new Thread(this::sendMessages, "Client Sender");
    sender.start();
    super.run();
  }

  @SneakyThrows
  private void sendMessages() {
    String message = "";
    while(shouldContinue) {
      message = consoleReader.readLine();
      if(message.equalsIgnoreCase("exit")) {
        closeClient();
      } else {
        sendText(message);
      }
    }
  }

  @SneakyThrows
  protected void receiveMessages() {
    String message = "";
    while(shouldContinue && (message = reader.readLine()) != null) {
      logger.info("[+] Message: {}", message);
    }
  }

  @SneakyThrows
  @Override
  protected void closeClient() {
    consoleReader.close();
    super.closeClient();
  }
}
