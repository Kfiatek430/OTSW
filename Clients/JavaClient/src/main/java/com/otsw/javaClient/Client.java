package com.otsw.javaClient;

import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class Client extends Thread {
    Socket socket;
    BufferedReader consoleReader;
    BufferedWriter writer;
    BufferedReader reader;
    private static final Logger logger = LogManager.getLogger(Client.class.getName());
    private boolean shouldContinue = true;

    @SneakyThrows
    public Client(int port) {
        logger.info("[-] Client is starting...");
        socket = new Socket("localhost", port);
        logger.info("[-] Client properties: " + socket);

        setupReaders();

        setName("Client");
        start();
    }

    public void run() {
        new Thread(this::sendMessages, "Client Sender").start();
        new Thread(this::reciveMessages, "Client Receiver").start();
    }

    @SneakyThrows
    public void setupReaders() {
        consoleReader = new BufferedReader(new InputStreamReader(System.in));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
