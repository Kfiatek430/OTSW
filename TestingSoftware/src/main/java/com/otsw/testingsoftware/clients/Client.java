package com.otsw.testingsoftware.clients;

import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Client extends Thread {
  Socket socket;
  BufferedReader consoleReader;
  BufferedWriter writer;
  BufferedReader reader;
  protected static final Logger logger = LogManager.getLogger(Client.class.getName());
  private int port = -1;
  private volatile boolean shouldContinue = true;
  protected volatile boolean isConnected = false;

  @SneakyThrows
  public Client(int port) {
    logger.info("[-] Client is starting...");
    socket = new Socket("localhost", port);
    this.port = port;
    logger.info("[-] Client properties: " + socket);
    setName("Client");
    start();
  }

  public void run() {
    isConnected = performHandshake();

    if(isConnected) {
//      new Thread(this::sendMessages, "Client Sender").start();
      new Thread(this::reciveMessages, "Client Receiver").start();
    }

  }

  @SneakyThrows
  private void setupBuffered() {
    consoleReader = new BufferedReader(new InputStreamReader(System.in));
    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
  }

  @SneakyThrows
  private boolean performHandshake() {
    setupBuffered();

    writer.write("GET / HTTP/1.1\n");
    writer.write("Host: http://localhost:" + port + "\n");
    writer.write("Upgrade: websocket\n");
    writer.write("Connection: Upgrade\n");

    byte[] randomBytes = new byte[16];
    new Random().nextBytes(randomBytes);
    String clientWebSocketKey = Base64.getEncoder().encodeToString(randomBytes);
    writer.write("Sec-WebSocket-Key: " + clientWebSocketKey + "\n");

    writer.write("Sec-WebSocket-Version: 13\n\n");
    writer.flush();

    Map<String, String> responseHeaders = readHandshake();
    String serverAcceptKey = responseHeaders.getOrDefault("Sec-WebSocket-Accept", "");

    if (responseHeaders.containsKey("HTTP/1.1 101 Switching Protocols") &&
      "websocket".equals(responseHeaders.getOrDefault("Upgrade", "")) &&
      "Upgrade".equals(responseHeaders.getOrDefault("Connection", ""))) {

      String concatenatedKey = clientWebSocketKey + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
      byte[] sha1HashedKey = MessageDigest.getInstance("SHA1").digest(concatenatedKey.getBytes());
      String computedAcceptKey = Base64.getEncoder().encodeToString(sha1HashedKey);

      if (computedAcceptKey.equals(serverAcceptKey)) {
        logger.info("[-] Client successfully connected to the server");
        return true;
      }
    }

    logger.warn("[!] Error in incoming handshake: {}", responseHeaders);
    return false;
  }

  @SneakyThrows
  private Map<String, String> readHandshake() {
    Map<String, String> requestHeaders = new HashMap<>();
    String line;
    while (!(line = reader.readLine()).isEmpty()) {
      if (line.indexOf(": ") > 0) {
        String[] headerParts = line.split(": ");
        requestHeaders.put(headerParts[0], headerParts[1]);
      } else {
        requestHeaders.put(line, "");
      }
    }
    return requestHeaders;
  }

  @lombok.SneakyThrows
  private void closeClient() {
    this.shouldContinue = false;
    consoleReader.close();

    writer.write("\n");
    writer.write((char) 0x88);
    writer.flush();

    reader.close();
    writer.close();
    socket.close();

    logger.info("[-] Unsubscribed from server");
  }

  @lombok.SneakyThrows
  private void sendMessages() {
    String message = "";
    while(shouldContinue) {
      message = consoleReader.readLine();
      if(message.equalsIgnoreCase("exit")) {
        closeClient();
      } else {
        writer.write(message + '\n');
        writer.flush();
      }
    }
  }

  @lombok.SneakyThrows
  protected void sendText(String message) {
    writer.write(message + '\n');
  }

  @lombok.SneakyThrows
  private void reciveMessages() {
    String message = "";
    while(shouldContinue && (message = reader.readLine()) != null) {
      logger.info("[+] Message: " + message);
    }
  }

  @SneakyThrows
  public void waitForConnection() {
    while(!isConnected) {
      Thread.sleep(100);
    }
  }
}

