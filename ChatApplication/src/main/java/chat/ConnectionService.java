package chat;

import messages.AuthMessage;
import messages.Message;
import messages.TextMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectionService {

    private final String SERVER_PATH = "localhost";
    private final int PORT = 8189;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private ChatFormController controller;
    private String login;
    private String nick;
    private boolean isAuth;
    private Thread readingThread;
    private MessageService messageService;
    private String errorDescription;

    public ConnectionService(ChatFormController controller) {
        this.controller = controller;
        this.messageService = new MessageService(this);
        this.isAuth = false;
    }

    public String getNick() {
        return nick;
    }

    public String getLogin() {
        return login;
    }

    public boolean isAuth() {
        return isAuth;
    }

    private void closeConnection() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        if (readingThread != null && readingThread.isAlive()) readingThread.interrupt();
        isAuth = false;
    }

    private void openConnection() throws IOException {

        socket = new Socket(SERVER_PATH, PORT);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        readingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] byteBuffer = new byte[1024];
                while (true) {
                    try {
                        int readByte = in.read(byteBuffer);
                        if (readByte != -1) {
                            Message message = messageService.getMessageFromStream(byteBuffer, readByte);
                            messageService.handleMessage(message);
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                        break;
                    } catch (Exception e) {
                        errorDescription = e.getMessage();
                        e.printStackTrace();
                        break;
                    }
                }
            }
        });
        readingThread.setDaemon(true);
        readingThread.start();

    }

    public void setAuth(String login, String nick) {
        this.login = login;
        this.nick = nick;
        this.isAuth = true;
    }

    public void sendMessageToServer(Message message) throws IOException {
        byte[] data = messageService.getSerializeMessage(message);
        out.write(data, 0, data.length);
        out.flush();
    }

    public void login(String login, String password, String nick) throws Exception {

        isAuth = false;
        openConnection();

        AuthMessage authMessage = new AuthMessage(login, password, nick);
        sendMessageToServer(authMessage);
        byte wait = 0;
        do {
            Thread.sleep(1000);
            wait++;
        } while (!isAuth && wait <= 5);

        if (!isAuth) {
            throw new Exception(errorDescription);
        }

    }

    public void sendTextMessageToServer(String msg) throws IOException {
        if (checkConnection()) {
            TextMessage textMessage = new TextMessage();
            textMessage.setMessage(msg);
            textMessage.setSource(login);
            textMessage.setSourceNick(nick);
            sendMessageToServer(textMessage);
        }
    }

    private boolean checkConnection() {
        if (!isAuth) {
            controller.sendMessageToFront("Login required", "System", "System", "");
            return false;
        }

        return true;
    }
}
