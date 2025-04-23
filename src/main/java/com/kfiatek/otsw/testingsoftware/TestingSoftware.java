package com.kfiatek.otsw.testingsoftware;

import com.kfiatek.otsw.clients.TestingClient;
import com.kfiatek.otsw.config.TestConfig;
import com.kfiatek.otsw.helpers.Helper;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class TestingSoftware extends Thread {
  TestConfig config;
  ArrayList<TestingClient> clients;
  protected static final Logger logger = LogManager.getLogger(TestingSoftware.class.getName());

  TestingSoftware(TestConfig config) {
    this.config = config;
  }

  public void run() {
    logger.info("[-] Running test: {}", config.testName);
    initializeClients();
    sendMessages();
  }

  @SneakyThrows
  void initializeClients() {
    for (int i = 0; i < config.clients; i++) { 
      clients.add(new TestingClient(i, config.connection.port, config.message.count, config.message.length, Helper.convert(config.message.characters), config.message.frequencyMs));
      Thread.sleep(config.connection.connectDelayMs);
    }
  }

  @SneakyThrows
  void sendMessages() {
    for (TestingClient client : clients) {
      client.start();
      Thread.sleep(config.connection.connectDelayMs);
    }
  }
}
