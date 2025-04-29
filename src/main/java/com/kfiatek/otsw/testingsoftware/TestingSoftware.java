package com.kfiatek.otsw.testingsoftware;

import com.kfiatek.otsw.clients.TestingClient;
import com.kfiatek.otsw.config.TestConfig;
import com.kfiatek.otsw.helpers.Helper;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class TestingSoftware extends Thread {
  private final TestConfig config;
  private final ArrayList<TestingClient> clients;
  private static final Logger logger = LogManager.getLogger(TestingSoftware.class.getName());
  private final CountDownLatch doneSignal;

  TestingSoftware(TestConfig config) {
    this.config = config;
    this.clients = new ArrayList<>();
    this.doneSignal = new CountDownLatch(config.clients);
  }

  public void run() {
    logger.info("[-] Running test: {}", config.testName);
    initializeClients();
    sendMessages();
  }

  @SneakyThrows
  void initializeClients() {

    for (int i = 0; i < config.clients; i++) {
      clients.add(new TestingClient(i, config.connection.port, config.message.count, config.message.length, Helper.convert(config.message.characters), config.message.frequencyMs, config.validation.timeoutMs, doneSignal));
      Thread.sleep(config.connection.connectDelayMs);
    }
  }

  @SneakyThrows
  void sendMessages() {
    for (TestingClient client : clients) {
      client.start();
      Thread.sleep(config.connection.connectDelayMs);
    }

    doneSignal.await();
    summarizeResults();
  }

  void summarizeResults() {
    int totalSuccess = 0;
    int totalFailure = 0;

    for (TestingClient client : clients) {
      totalSuccess += client.getSuccessCount();
      totalFailure += client.getFailureCount();
    }

    logger.info("[=] Test summary:");
    logger.info("[=] Messages within timeout: {}", totalSuccess);
    logger.info("[=] Messages exceeding timeout: {}", totalFailure);
  }
}
