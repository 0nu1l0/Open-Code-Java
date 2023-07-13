package Chat;

public class AuthorizationCommand extends Command{
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public AuthorizationCommand(String nickname) {
        this.nickname = nickname;
    }
}
