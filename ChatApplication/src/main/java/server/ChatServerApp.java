package server;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class ChatServerApp {

    public static void main(String[] args) {
        new Thread(new ChatServer()).start();

        try {
            DBConnection db = DBConnection.getInstance();
            System.out.println(db.addUser("user", "pass", "nick"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
