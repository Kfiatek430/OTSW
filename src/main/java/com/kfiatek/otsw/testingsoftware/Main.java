package com.kfiatek.otsw.testingsoftware;

import com.kfiatek.otsw.config.ConfigLoader;
import com.kfiatek.otsw.config.TestConfig;
import lombok.SneakyThrows;

public class Main {
  @SneakyThrows
  public static void main(String[] args) {
    TestConfig config = ConfigLoader.load("testcases/multi_client_char_mix_test.yaml");
//    TestConfig config = ConfigLoader.load("testcases/high_frequency_test.yaml");
//    TestConfig config = ConfigLoader.load("testcases/stress_test_no_delay.yaml");
    TestingSoftware testingSoftware = new TestingSoftware(config);
    testingSoftware.start();
  }
}