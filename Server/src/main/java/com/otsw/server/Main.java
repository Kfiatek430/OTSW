package com.otsw.server;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
  @SneakyThrows
  public static void main(String[] args) {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    System.out.print("Enter port: ");
    int port = Integer.parseInt(reader.readLine());
    new Server(port);
  }
}