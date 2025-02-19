package com.otsw.server;

import lombok.SneakyThrows;

import org.apache.logging.log4j.*;

import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.util.*;

public class Connection extends Thread {
  private final int id;
  private final Server server;
  private final Socket connection;
  private BufferedReader reader;
  private BufferedWriter writer;
  private static final Logger logger = LogManager.getLogger(Connection.class.getName());

  public Connection(Server server, Socket connection, int port, int id) {
    this.server = server;
    this.connection = connection;
    this.id = id;
    setName("Connection." + port + '.' + id);
    logger.info("[-] Socket connected {}", this);
  }

  @SneakyThrows
  private void setupBuffered() {
    this.reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    this.writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
  }

  @SneakyThrows
  boolean performHandshake() {
    setupBuffered();
    Map<String,String> handshake = readHandshake();
    String wsKey = handshake.getOrDefault("Sec-WebSocket-Key", "");

    if (handshake.containsKey("GET / HTTP/1.1") &&
        handshake.containsKey("Host") /* TODO sprawdzić zgodność adresu */ &&
        handshake.getOrDefault("Upgrade","").equals("websocket") &&
        handshake.getOrDefault("Connection", "").equals("Upgrade") &&
        wsKey.length() == 24 &&
        handshake.getOrDefault("Sec-WebSocket-Version", "").equals("13")) {

      writer.write("HTTP/1.1 101 Switching Protocols\n");
      writer.write("Upgrade: websocket\n");
      writer.write("Connection: Upgrade\n");

      String keyResp = wsKey + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
      byte[] sha1KeyResp = MessageDigest.getInstance("SHA1").digest((keyResp).getBytes());
      String base64KeyResp = Base64.getEncoder().encodeToString(sha1KeyResp);
      writer.write("Sec-WebSocket-Accept: " + base64KeyResp + "\n\n");
      writer.flush();

      return true;
    } else {
      logger.warn("Error in incomming handshake: {}", handshake);
      connection.close();
      return false;
    }

  }

  @SneakyThrows
  private Map<String,String> readHandshake() {
    Map<String,String> handshake = new HashMap<>();
    String line;
    while(!(line = reader.readLine()).isEmpty()) {
      if (line.indexOf(": ")> 0) {
        String[] kv = line.split(": ");
        handshake.put(kv[0], kv[1]);
      } else {
        handshake.put(line, "");
      }
    }
    return handshake;
  }

  @SneakyThrows
  public void run() {
    readMessages();
  }

  @SneakyThrows
  public void readMessages() {
    String message = "";
    while ((message = reader.readLine()) != null) {
        logger.info("Received message: {}", message);  // Logowanie wiadomości
        if (isClosingHandshake()) break;

        server.broadcastMessages(message);
    }

    server.unsubscribe(this);
    connection.close();
    reader.close();
    writer.close();
  }

  @SneakyThrows
  public void writeMessage(String str) {
    writer.write(str + '\n');
    writer.flush();
  }

  @SneakyThrows
  public boolean isClosingHandshake() {
    connection.setSoTimeout(10);
    try {
      int firstByte = reader.read();
      if(firstByte == -1) {
        logger.warn("[!] Connection closed unexpectedly");
        return true;
      }

      int opcode = firstByte & 0x0F;
      if(opcode == 8) {
        logger.info("[-] Received closing handshake");
        return true;
      }

      return false;
    } catch (SocketTimeoutException ignored) {
      return false;
    } finally {
      connection.setSoTimeout(0);
    }
  }
}
