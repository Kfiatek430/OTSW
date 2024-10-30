import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        System.out.println("[-] Server is starting");

        try {
            ServerSocket serverSocket = new ServerSocket(8000);
            System.out.printf("[-] Server properties: %s\n", serverSocket);

            Socket socket = serverSocket.accept();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            String str = dataInputStream.readUTF();

            System.out.printf("[-] Message: %s\n", str);

            socket.close();
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }
}