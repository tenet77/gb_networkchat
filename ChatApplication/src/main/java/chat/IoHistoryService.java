package chat;

import messages.Message;
import messages.TextMessage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface IoHistoryService {

    public List<TextMessage> getHistory();
    public List<TextMessage> getHistory(int limit) throws IOException;
    public void saveHistory(Message history) throws IOException;

}
