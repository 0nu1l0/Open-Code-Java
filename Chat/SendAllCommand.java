package Chat;

public class SendAllCommand extends Command {
    private final String message;

    public String getMessage() {
        return message;
    }

    public SendAllCommand(String message) {
        this.message = message;
    }
}
