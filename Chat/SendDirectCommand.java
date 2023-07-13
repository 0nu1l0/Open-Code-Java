package Chat;

public class SendDirectCommand extends Command {
    private final String message;
    private String destinationUser;

    public String getMessage() {
        return message;
    }

    public String getDestinationUser() {
        return destinationUser;
    }

    public SendDirectCommand(String message, String nickname) {
        this.message = message;
        destinationUser = nickname;
    }
}
