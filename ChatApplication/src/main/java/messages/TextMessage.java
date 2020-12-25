package messages;

public class TextMessage extends Message{

    private String sourceNick;

    public TextMessage() {
        super(TypeMessage.TextMessage);
    }

    public void setSourceNick(String sourceNick) {
        this.sourceNick = sourceNick;
    }

    public String getSourceNick() {
        return sourceNick;
    }
}
