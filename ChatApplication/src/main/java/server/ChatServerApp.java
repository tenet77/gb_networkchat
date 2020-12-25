package server;

public class ChatServerApp {

    public static void main(String[] args) {
        new Thread(new ChatServer()).start();

    }

}
