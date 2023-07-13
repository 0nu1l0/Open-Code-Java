package Chat;

public class BlockCommand extends Command{
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public BlockCommand(String nickname) {
        this.nickname = nickname;
    }
}
