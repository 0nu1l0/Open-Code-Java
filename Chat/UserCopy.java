package Chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class UserCopy {
    private Socket clientSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public UserCopy() throws IOException {
        clientSocket = new Socket("localhost", 4004);
        try {
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            new SendMessage().start();
            new ReadMessage().start();
        }
        catch (IOException e) {

        }
    }

    private class SendMessage extends Thread {
        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter your nickname: ");
            String nickname = scanner.nextLine();
            AuthorizationCommand authorizationCommand = new AuthorizationCommand(nickname);
            try {
                outputStream.writeObject(authorizationCommand);
                outputStream.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            while (true) {
                String userText;
                try {
                    System.out.println("1 - send direct");
                    System.out.println("2 - send all");
                    System.out.println("3 - block user");
                    System.out.println("stop");
                    userText = scanner.nextLine();
                    Command command = null;
                    if (userText.equals("1")) {
                        System.out.println("Enter your message: ");
                        String message = scanner.nextLine();
                        System.out.println("Enter user to send: ");
                        String destinationNickname = scanner.nextLine();
                        command = new SendDirectCommand(message, destinationNickname);
                    }
                    if (userText.equals("2")) {
                        System.out.println("Enter your message: ");
                        String message = scanner.nextLine();
                        command = new SendAllCommand(message);
                    }
                    if (userText.equals("3")) {
                        System.out.println("Enter nickname to block: ");
                        String nicknameToBan = scanner.nextLine();
                        command = new BlockCommand(nicknameToBan);
                    }
                    if (userText.equals("stop")) {
                        break;
                    }
                    outputStream.writeObject(command);
                    outputStream.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private class ReadMessage extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Command command = (Command) inputStream.readObject();
                    if (command instanceof SendAllCommand p) {
                        System.out.println("All message: " + p.getMessage());
                    }
                    else if (command instanceof  SendDirectCommand p) {
                        System.out.println("Direct message from " + p.getDestinationUser() + ": " + p.getMessage());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    public static void main(String[] args) throws IOException {
        User user = new User();
    }
}
