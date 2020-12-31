package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import messages.*;

public class ChatServer implements Runnable {

    private final int PORT = 8189;
    private final ConcurrentLinkedDeque<ClientHandler> clients;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public ChatServer() {
        this.clients = new ConcurrentLinkedDeque<>();
    }

    private synchronized void addClient(ClientHandler client) {
        clients.add(client);
    }

    public synchronized void removeClient(ClientHandler client) {
        if (clients.contains(client)) {
            clients.remove(client);
        }
    }

    @Override
    public void run() {
        try {
            ServerSocket server = new ServerSocket(PORT);
            while (true) {
                System.out.println("Server wait connection");
                Socket socket = server.accept();
                System.out.println("Connection accepted");
                ClientHandler client = new ClientHandler(this, socket);
                executor.execute(client);
                addClient(client);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }

    public void broadCast(Message msg, ClientHandler forClient) throws IOException {
        byte[] data = MessageService.getInstance().getSerializeMessage(msg);
        if (forClient != null) {
            forClient.sendMessage(data);
        } else {
            for (ClientHandler client : clients) {
                client.sendMessage(data);
            }
        }
    }

    public ClientHandler getClientByLogin(String destination) {
        for (ClientHandler client : clients) {
            if (client.getLogin().equals(destination)) return client;
        }

        return null;
    }
}
