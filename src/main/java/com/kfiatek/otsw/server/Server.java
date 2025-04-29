package com.kfiatek.otsw.server;

import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class Server extends Thread {
  private int clientId = 0;
  private final ServerSocket serverSocket;
  private final Set<Connection> connections = new HashSet<>();
  private static final Logger logger = LogManager.getLogger(Server.class.getName());
  static final String address = "127.0.0.1";
  int counter = 0;

  @SneakyThrows
  public Server(int port) {
    logger.info("[-] Server is starting...");
    serverSocket = new ServerSocket(port);
    logger.info("[-] Server properties: {}", serverSocket);

    setName("Server." + port);
    start();
  }

  @SneakyThrows
  @Override
  public void run() {
    logger.info("[-] Waiting for connections...");
    while (true) {
      clientId++;
      Connection conn = new Connection(this, serverSocket.accept(), serverSocket.getLocalPort(), clientId);
      if (conn.performHandshake()) {
        conn.start();
        connections.add(conn);
        logger.info("[-] Connected clients: {}", connections.size());
      }
    }
  }

  public void unsubscribe(Connection conn) {
    logger.info("[-] Unsubscribe client {}", conn);
    connections.remove(conn);
    logger.info("[-] Connected clients: {}", connections.size());
  }

  public void broadcastMessages(String message) {
    counter++;
    logger.info("[+][{}] Message: {}", counter, message);
    connections.forEach(connection -> connection.writeMessage(message));
  }
}
