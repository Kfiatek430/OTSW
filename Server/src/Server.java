import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.HashSet;

public class Server {
    ServerSocket server;
    Set<Connection> connections = new HashSet<>();
    public Server() throws IOException {
        System.out.println("[-] Server is starting");

        server = new ServerSocket(8000);
        System.out.printf("[-] Server properties: %s\n", server);

        listener();
    }

    public void listener() throws IOException {
        System.out.println("[-] Waiting for connections...");
        while (true) {
            Socket client = server.accept();
            Connection connection = new Connection(client);
            connection.start();
            this.connections.add(connection);
        }
    }
}
