package chat;

import messages.TextMessage;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {

    private static DBConnection instance;

    private final String USER = "java";
    private final String PASS = "!1qaz@2wsx";
    private final String DBNAME = "java";
    private Connection connection;
    private Statement statement;

    public static DBConnection getInstance() throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (instance == null) {
            instance = new DBConnection();
        }

        return instance;
    }

    public DBConnection() throws SQLException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        checkConnection();
    }

    public boolean addMessage(TextMessage message, String login) throws SQLException {
        String sqlCommand = String.format("INSERT INTO messages (timestamp, login, source, sourceNick, dest, message, isNew) " +
                        "VALUES(%d,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\", true)",
                System.currentTimeMillis(), login, message.getSource(), message.getSourceNick(), message.getDestination(), message.getMessage());

        checkConnection();

        int rows = statement.executeUpdate(sqlCommand);
        if (rows > 0) return true;

        return false;
    }

    public List<TextMessage> getNewMessage(String login) throws SQLException {
        String sqlCommand = String.format("SELECT * FROM messages WHERE isNew AND login=\"%s\" ORDER BY timestamp;", login);
        List<TextMessage> result = new ArrayList<>();
        String idForUpdate = "";

        checkConnection();
        ResultSet select = statement.executeQuery(sqlCommand);
        while (select.next()) {
            TextMessage newMessage = new TextMessage();
            newMessage.setSource(select.getString("source"));
            newMessage.setDestination(select.getString("dest"));
            newMessage.setMessage(select.getString("message"));
            newMessage.setSourceNick(select.getString("sourceNick"));

            result.add(newMessage);

            idForUpdate += ""+select.getInt("id")+",";
        }
        idForUpdate += "0";
        sqlCommand = String.format("UPDATE messages SET isNew = false WHERE id IN (%s) AND login=\"%s\"", idForUpdate, login);
        statement.executeUpdate(sqlCommand);

        return result;

    }

    private void checkConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/"+DBNAME+"?serverTimezone=Europe/Moscow&useSSL=false",
                    USER, PASS);
        }

        if (statement == null || statement.isClosed()) {
            statement = connection.createStatement();
        }
    }
}
