package messages;

import java.io.Serializable;

abstract public class Message implements Serializable {

    private String source;
    private String destination;
    private TypeMessage type;
    private String message;

    public Message(TypeMessage type) {
        this.type = type;
        this.message = "";
        this.source = "";
        this.destination = "";
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public TypeMessage getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
