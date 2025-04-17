package com.kfiatek.otsw.clients;

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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Client extends Thread {
  private final Socket socket;
  private BufferedWriter writer;
  protected BufferedReader reader;
  protected static final Logger logger = LogManager.getLogger(Client.class.getName());
  private int port = -1;
  protected volatile boolean shouldContinue = true;
  protected volatile boolean isConnected = false;
  private final BlockingQueue<String> messages = new LinkedBlockingQueue<>();

  @SneakyThrows
  public Client(int port) {
    logger.info("[-] Client is starting...");
    socket = new Socket("localhost", port);
    this.port = port;
    logger.info("[-] Client properties: " + socket);
    setName("Client");
    start();
  }

  @SneakyThrows
  public void sendText(String msg) {
    writer.write(msg + '\n');
    writer.flush();
  }

  public void run() {
    isConnected = performHandshake();
    if(isConnected) receiveMessages();
  }

  @SneakyThrows
  protected void setupBuffered() {
    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
  }

  @SneakyThrows
  protected boolean performHandshake() {
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

  @SneakyThrows
  void closeClient() {
    this.shouldContinue = false;

    writer.write("\n");
    writer.write((char) 0x88);
    writer.flush();

    reader.close();
    writer.close();
    socket.close();

    logger.info("[-] Unsubscribed from server");
  }

  @SneakyThrows
  protected void receiveMessages() {
    String message = "";
    while(shouldContinue && (message = reader.readLine()) != null) {
      messages.add(message);
    }
  }

  @SneakyThrows
  public String readLine(long milliseconds) {
    return messages.poll(milliseconds, TimeUnit.MILLISECONDS);
  }

  @SneakyThrows
  public void waitForConnection() {
    while(!isConnected) {
      Thread.sleep(100);
    }
  }
}

