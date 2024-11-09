import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        System.out.println("[-] Client is starting");
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

        try {
            Socket socket = new Socket("localhost", 8000);
            System.out.println(socket);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String message = "";
            while(!message.equals("exit")) {
                message = consoleReader.readLine();
                writer.write(message + "\n");
                writer.flush();
            }

            writer.close();
            socket.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}