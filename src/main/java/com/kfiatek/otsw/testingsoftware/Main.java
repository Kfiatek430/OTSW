package com.kfiatek.otsw.testingsoftware;

import com.kfiatek.otsw.config.ConfigLoader;
import com.kfiatek.otsw.config.TestConfig;
import lombok.SneakyThrows;

public class Main {
  @SneakyThrows
  public static void main(String[] args) {
    TestConfig config = ConfigLoader.load("testcases/basic_test.yaml");
    TestingSoftware testingSoftware = new TestingSoftware(config);
    testingSoftware.start();
  }
}