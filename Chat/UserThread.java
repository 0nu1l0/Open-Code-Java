package Chat;

import java.net.Socket;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class UserThread extends Thread{
    private final Socket clientSocket;
    private String nickname;
    private ObjectInputStream clientInputStream;
    private ObjectOutputStream clientOutputStream;
    private static List<UserThread> users = new ArrayList<>();
    private HashSet<UserThread> blockedUsers = new HashSet<>();
    public UserThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
        users.add(this);
    }

    @Override
    public void run() {
        try {
            clientOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            clientInputStream = new ObjectInputStream(clientSocket.getInputStream());
            AuthorizationCommand authorizationCommand = (AuthorizationCommand) clientInputStream.readObject();
            nickname = authorizationCommand.getNickname();
            while (true) {
                Command command = (Command) clientInputStream.readObject();
                if (command instanceof SendAllCommand) {
                    for (UserThread user : users) {
                        if (user != this && !user.blockedUsers.contains(this))
                            user.send(command);
                    }
                }
                else if (command instanceof SendDirectCommand p) {
                    Optional<UserThread> user = users.stream().filter(t -> t.nickname.equals(p.getDestinationUser())).findFirst();
                    if (user.isPresent() && !user.get().blockedUsers.contains(this)) {
                        SendDirectCommand t = new SendDirectCommand(p.getMessage(), nickname);
                        user.get().send(t);
                    }
                }
                else if (command instanceof BlockCommand p) {
                    Optional<UserThread> user = users.stream().filter(t -> t.nickname.equals(p.getNickname())).findFirst();
                    user.ifPresent(userThread -> blockedUsers.add(userThread));
                }
            }
        }
        catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(Command command) throws IOException {
        clientOutputStream.writeObject(command);
        clientOutputStream.flush();
    }
}
