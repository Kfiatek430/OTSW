package com.otsw.server;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
  @SneakyThrows
  public static void main(String[] args) {
    new Server(8000);
  }
}