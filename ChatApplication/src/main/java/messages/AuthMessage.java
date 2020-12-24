package messages;

public class AuthMessage extends Message {

    private String login;
    private String password;
    private String nick;
    private boolean isAuth;

    public AuthMessage(String login, String password, String nick) {
        super(TypeMessage.Auth);
        this.login = login;
        this.password = password;
        this.nick = nick;
        this.isAuth = false;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getNick() {
        return nick;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }
}
