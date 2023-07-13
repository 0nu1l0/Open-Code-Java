package Chat;

import ServerClientMath.ClientThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(4004);
        System.out.println("Server started");
        while (true) {
            Socket clientSocket = server.accept();
            UserThread thread = new UserThread(clientSocket);
            thread.start();
        }
    }
}
