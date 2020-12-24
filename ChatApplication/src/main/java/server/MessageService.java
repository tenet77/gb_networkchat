package server;

import java.io.*;
import messages.*;

public class MessageService {

    private static MessageService instance;

    public static MessageService getInstance() {
        if (instance == null) {
            instance = new MessageService();
        }

        return instance;
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
        try  {
            message = (Message) objectInputStream.readObject();
        } catch (Exception e) {
            message = null;
        }

        return message;
    }


}
