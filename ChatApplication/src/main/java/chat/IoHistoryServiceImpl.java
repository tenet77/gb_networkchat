package chat;

import messages.Message;
import messages.TextMessage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class IoHistoryServiceImpl implements IoHistoryService {

    private String fileName;

    public IoHistoryServiceImpl(String fileName) throws FileNotFoundException {
        this.fileName = fileName;
    }

    @Override
    public List<TextMessage> getHistory(int limit) throws IOException {
        List<TextMessage> list = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists()) return list;

        InputStream is = new FileInputStream(fileName);
        ObjectInputStream objectInputStream = new ObjectInputStream(is);
        while (true) {
            try  {
                Object obj = objectInputStream.readObject();
                if (obj instanceof TextMessage) {
                    list.add((TextMessage) obj);
                }
            } catch (Exception e) {
                break;
            }
        }
        return list;

    }

    @Override
    public List<TextMessage> getHistory() {
        return null;
    }

    @Override
    public void saveHistory(Message message) throws IOException {
        OutputStream os = new FileOutputStream(fileName, true);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(os);
        objectOutputStream.writeObject(message);
        os.flush();
    }
}
