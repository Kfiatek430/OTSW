package com.otsw.javaClient;

import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Random;

public class Client extends Thread {
    Socket socket;
    BufferedReader consoleReader;
    BufferedWriter writer;
    BufferedReader reader;
    private static final Logger logger = LogManager.getLogger(Client.class.getName());
    private int port = -1;
    private boolean shouldContinue = true;

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
        new Thread(this::sendMessages, "Client Sender").start();
        new Thread(this::reciveMessages, "Client Receiver").start();
    }

    @SneakyThrows
    public void setupBuffered() {
        consoleReader = new BufferedReader(new InputStreamReader(System.in));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @SneakyThrows
    public void performHandshake() {
        setupBuffered();

        writer.write("GET / HTTP/1.1");
        writer.write("Upgrade:  websocket");
        writer.write("http://localhost:" + port);

        byte[] array = new byte[16];
        new Random().nextBytes(array);
        String encoded = new String(Base64.getEncoder().encode(array));
        writer.write("Sec-WebSocket-Key: " + encoded);

        writer.write("Sec-WebSocket-Version: 13");
        writer.write("Connection: Upgrade\n\n");
    }

    @lombok.SneakyThrows
    private void sendMessages() {
        String message = "";
        while(shouldContinue) {
            message = consoleReader.readLine();
            writer.write(message + '\n');
            writer.flush();
        }
    }

    @lombok.SneakyThrows
    private void reciveMessages() {
        String message = "";
        while(shouldContinue) {
            message = reader.readLine();
            logger.info("[+] Message: " + message);
        }
    }
}
