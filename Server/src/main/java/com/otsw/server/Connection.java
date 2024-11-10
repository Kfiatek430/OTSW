package com.otsw.server;

import lombok.SneakyThrows;

import org.apache.logging.log4j.*;

import java.io.*;
import java.net.*;

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
        logger.info("[-] Socket connected " + this);
    }

    @SneakyThrows
    public void run() {
        setupBuffered();
        readMessages();
    }

    @SneakyThrows
    private void setupBuffered() {
        this.reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
    }

    @SneakyThrows
    private void readMessages() {
        String message = "";
        while (!message.equals("exit")) {
            try {
                message = reader.readLine();
            } catch (SocketException e) {
                server.unsubscribe(this);
                connection.close();
                reader.close();
                writer.close();
                return;
            }
            server.broadcastMessages(message);
        }
    }

    @SneakyThrows
    public void writeMessage(String str) {
        writer.write(str + '\n');
        writer.flush();
    }
}
