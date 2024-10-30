import java.io.DataOutputStream;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        System.out.println("[-] Client is starting");

        try {
            Socket socket = new Socket("localhost", 8000);
            System.out.printf("[-] Socket properties: %s", socket);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            dataOutputStream.writeUTF("Hello!");
            dataOutputStream.flush();

            dataOutputStream.close();
            socket.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}