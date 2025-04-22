package com.kfiatek.otsw.testingsoftware;

import com.kfiatek.otsw.clients.Client;
import com.kfiatek.otsw.clients.TestingClient;
import com.kfiatek.otsw.config.TestConfig;
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
  }

  @SneakyThrows
  void initializeClients() {
    for (int i = 0; i < config.clients; i++) { 
      clients.add(new TestingClient(config.connection.port));
      Thread.sleep(config.connection.connectDelayMs);
    }
  }
}
