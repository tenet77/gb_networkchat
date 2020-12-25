package chat;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import messages.TextMessage;
import netscape.javascript.JSObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ChatFormController implements Initializable {

    public WebView messageBox;
    public TextField messageField;
    public TextField textLogin;
    public PasswordField textPassword;
    public Pane paneLogin;
    public TextField textNick;
    public Label labelLogin;
    public Label labelNick;
    public Pane paneInfo;
    private JSObject jsConnector;
    private JavaConnector javaConnector;
    private ConnectionService connectionService;
    private ThreadReadMessage readingThread;

    public void loginAgain() {
        paneInfo.setVisible(false);
        paneLogin.setVisible(true);
    }

    private class ThreadReadMessage implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    if (jsConnector != null) {
                        List<TextMessage> newMessage = DBConnection.getInstance().getNewMessage(connectionService.getLogin());
                        for (TextMessage message : newMessage) {
                            sendMessageToFront(message.getMessage(), message.getSource(), message.getSourceNick(),"");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    private void startReadMessage() {

        readingThread = new ThreadReadMessage();
        Thread t = new Thread(readingThread);
        t.setDaemon(true);
        t.start();

    }

    public void sendMessage() throws IOException {
        String msg = messageField.getText();
        messageField.clear();
        messageField.requestFocus();
        connectionService.sendTextMessageToServer(msg);
    }

    public void sendMessageToFront(String msg, String name, String nick, String align) {

        if (align.isEmpty()) {
            align = "left";
            if (name.equals(connectionService.getLogin())) {
                align = "right";
            }
        }

        String finalAlign = align;
        Platform.runLater(()-> jsConnector.call("showMessage", nick, finalAlign, msg));
    }

    public void login() {
        try {
            connectionService.login(textLogin.getText(), textPassword.getText(), textNick.getText());
            paneLogin.setVisible(false);
            paneInfo.setVisible(true);
            labelLogin.setText("Login: " + connectionService.getLogin());
            labelNick.setText("Nick: " + connectionService.getNick());
        } catch (Exception e) {
            sendMessageToFront(e.getMessage(), "System", "System", "");
            textLogin.requestFocus();
            labelLogin.setText("Login: ");
            labelNick.setText("Nick: ");
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

        startReadMessage();

    }
}
