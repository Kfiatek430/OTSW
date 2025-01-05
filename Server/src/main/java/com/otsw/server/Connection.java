package com.otsw.server;

import lombok.SneakyThrows;

import org.apache.logging.log4j.*;

import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Optional;

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
    ArrayList<String> handshakeLines = readHandshake();
    String key = handshakeLines
        .stream()
        .filter(l -> l.startsWith("Sec-WebSocket-Key: "))
        .findFirst()
        .map(l -> l.substring(19))
        .orElse("");

    if (key.length() == 24) {
      writer.write("HTTP/1.1 101 Switching Protocols\n");
      writer.write("Upgrade: websocket\n");
      writer.write("Connection: Upgrade\n");

      String keyResp = key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
      byte[] Sha1KeyResp = MessageDigest.getInstance("SHA1").digest((keyResp).getBytes());
      String Base64KeyResp = Base64.getEncoder().encodeToString(Sha1KeyResp);
      writer.write("Sec-WebSocket-Accept: " + Base64KeyResp + "\n\n");

      writer.flush();
      return true;
    }

    connection.close();
    return false;
  }


  @SneakyThrows
  private ArrayList<String> readHandshake() {
    ArrayList<String> handshakeLines = new ArrayList<>();
    String line;
    while(!(line = reader.readLine()).isEmpty()) {
      handshakeLines.add(line);
    }
    return handshakeLines;
  }

  @SneakyThrows
  public void run() {
    readMessages();
  }

  @SneakyThrows
  private void readMessages() {
    String message = "";
    while (message == null || !message.equals("exit")) {
      try {
        message = reader.readLine();
      } catch (SocketException e) {
        server.unsubscribe(this);
        connection.close();
        reader.close();
        writer.close();
        return;
      }
      if (message != null) server.broadcastMessages(message);
    }
  }

  @SneakyThrows
  public void writeMessage(String str) {
    writer.write(str + '\n');
    writer.flush();
  }
}
