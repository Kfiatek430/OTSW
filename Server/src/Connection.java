import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Connection extends Thread {
    Socket connection;
    BufferedReader reader;

    public Connection(Socket connection) {
        this.connection = connection;
        System.out.println("[-] Socket connected");
    }

    @Override
    public void run() {
        try {
            setupReader();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            readMessages();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupReader() throws IOException {
        this.reader = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
    }

    private void readMessages() throws IOException {
        String message = "";
        while (!message.equals("exit")) {
            message = this.reader.readLine();
            System.out.printf("[+] Message: %s\n", message);
        }
    }
}
