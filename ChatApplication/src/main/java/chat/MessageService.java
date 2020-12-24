package chat;

import messages.AuthMessage;
import messages.Message;
import messages.TextMessage;

import java.io.*;

public class MessageService {

    private ConnectionService connectionService;

    public MessageService(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    public byte[] getSerializeMessage(Message message) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(message);
        return byteArrayOutputStream.toByteArray();
    }

    public Message getMessageFromStream(byte[] byteBuffer, int readByte) throws IOException, ClassNotFoundException {

        Message message;

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteBuffer, 0, readByte);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        try {
            message = (Message) objectInputStream.readObject();
        } catch (Exception e) {
            message = null;
        }
        return message;
    }

    public void handleMessage(Message message) throws Exception {
        if (message instanceof AuthMessage) {
            handleAuthMessage((AuthMessage) message);
        } else if (message instanceof TextMessage) {
            connectionService.showTextMessage(message);;
        }
    }

    private void handleAuthMessage(AuthMessage message) throws Exception {
        if (message.isAuth()) {
            connectionService.setAuth(message.getLogin(), message.getNick());
        } else {
            throw new Exception(message.getMessage());
        }
    }
}
