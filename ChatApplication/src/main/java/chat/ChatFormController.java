package chat;

import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatFormController implements Initializable {

    public WebView messageBox;
    public TextField messageField;
    public TextField textLogin;
    public PasswordField textPassword;
    public Pane paneLogin;
    public TextField textNick;
    private JSObject jsConnector;
    private JavaConnector javaConnector;
    private ConnectionService connectionService;

    public void sendMessage() throws IOException {
        String msg = messageField.getText();
        messageField.clear();
        messageField.requestFocus();
        sendMessageToFront(msg, connectionService.getNick());
        connectionService.sendTextMessageToServer(msg);
    }

    public void sendMessageTo(String msg, String name) {
        sendMessageToFront(msg, name);
    }

    public void sendMessageToFront(String msg, String name) {
        String align = "left";
        if (name.equals(connectionService.getNick())) {
            align = "right";
        }
        jsConnector.call("showMessage", name, align, msg);
    }

    public void login(ActionEvent actionEvent) {
        try {
            connectionService.login(textLogin.getText(), textPassword.getText(), textNick.getText());
            paneLogin.setVisible(false);
        } catch (Exception e) {
            sendMessageToFront(e.getMessage(), "System");
            textLogin.requestFocus();
        } finally {
            textLogin.clear();
            textPassword.clear();
            textNick.clear();
        }
    }

    public class JavaConnector {
        public void toLowerCase(String value) {
            if (value != null) {
                jsConnector.call("showResult", value.toLowerCase());
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        WebEngine webEngine = messageBox.getEngine();

        javaConnector = new JavaConnector();

        webEngine.
                getLoadWorker().
                stateProperty().
                addListener((observable, oldValue, newValue) -> {
                    if (Worker.State.SUCCEEDED == newValue) {
                        JSObject window = (JSObject) webEngine.executeScript("window");
                        window.setMember("javaConnector", javaConnector);

                        jsConnector = (JSObject) webEngine.executeScript("getJsConnector()");
                    }
                });

        webEngine.load(getClass().getResource("html/message_tr.html").toString());

        connectionService = new ConnectionService(this);

    }
}
