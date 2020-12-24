package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.sql.SQLException;

import messages.*;

public class ClientHandler implements Runnable {

    public static int count = 0;

    private final ChatServer server;
    private final Socket socket;
    private DataInputStream is;
    private DataOutputStream os;
    private String login;
    private String nick;
    private boolean authOk;
    private boolean running;

    public ClientHandler(ChatServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
        this.authOk = false;
        this.running = true;
    }


    public void sendMessage(byte[] msg) throws IOException {
        os.write(msg, 0, msg.length);
        os.flush();
    }

    public String getLogin() {
        return login;
    }

    public boolean isAuthOk() {
        return authOk;
    }

    @Override
    public void run() {
        try {
            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());
            byte[] byteBuffer = new byte[1024];
            Message message;
            while (running) {
                try {
                    int readByte = is.read(byteBuffer);
                    if (readByte != -1) {
                        System.out.println("Get data");
                        message = MessageService.getInstance().getMessageFromStream(byteBuffer, readByte);
                        System.out.println("Get message: " + message);
                        handleMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(login + ": client crashed");
                    server.removeClient(this);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleMessage(Message msg) throws IOException, NoSuchMethodException, InstantiationException, SQLException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        if (msg == null) return;

        if (msg instanceof AuthMessage) {
            handleAuthMessage((AuthMessage) msg);
        }else if (msg instanceof TextMessage) {
            handleTextMessage((TextMessage) msg);
        }
    }

    private void handleTextMessage(TextMessage msg) throws IOException {
        if (msg.getDestination().isEmpty()) {
            server.broadCast(msg, null);
        } else {
            ClientHandler client = server.getClientByLogin(msg.getDestination());
            if (client != null) {
                server.broadCast(msg, client);
            }
        }
    }

    private void handleAuthMessage(AuthMessage msg) throws IOException, NoSuchMethodException, InstantiationException, SQLException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        if (DBConnection.getInstance().checkAuth(msg.getLogin(), msg.getPassword())) {

            this.login = msg.getLogin();
            this.nick = msg.getNick();
            if (this.nick.isEmpty()) {
                this.nick = "User#" + ClientHandler.count;
                ClientHandler.count++;
                msg.setNick(this.nick);
            }
            this.authOk = true;
            msg.setAuth(true);
            DBConnection.getInstance().updateUser(this.login, msg.getPassword(), this.nick);
        } else {
            msg.setMessage("Check auth failed");
        }
        server.broadCast(msg, this);
    }
}
