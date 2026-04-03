package com.kfiatek.otsw.testingsoftware;

import com.kfiatek.otsw.config.ConfigLoader;
import com.kfiatek.otsw.config.TestConfig;
import lombok.SneakyThrows;

public class Main {
  @SneakyThrows
  public static void main(String[] args) {
    if (args.length == 0) {
      System.err.println("Usage: java Main <testcase-url-or-path>");
      System.err.println("Example: java Main testcases/multi_client_char_mix_test.yaml");
      System.exit(1);
    }
    
    String configPath = args[0];
    TestConfig config = ConfigLoader.load(configPath);
    TestingSoftware testingSoftware = new TestingSoftware(config);
    testingSoftware.start();
  }
}