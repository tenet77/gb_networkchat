package server;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;

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

    public boolean addUser(String login, String pass, String nick) throws SQLException {
        String sqlCommand = String.format("INSERT INTO users (login, password, nick) VALUES(\"%s\",\"%s\",\"%s\")",
                login, pass, nick);

        checkConnection();

        int rows = statement.executeUpdate(sqlCommand);
        if (rows > 0) return true;

        return false;
    }

    public boolean updateUser(String login, String pass, String nick) throws SQLException {
        String sqlCommand = String.format("UPDATE users SET password = \"%s\", nick = \"%s\" WHERE login=\"%s\";",
                pass, nick, login);

        checkConnection();

        int rows = statement.executeUpdate(sqlCommand);
        if (rows > 0) return true;

        return false;
    }

    public boolean checkAuth(String login, String pass) throws SQLException {
        String sqlCommand = String.format("SELECT id FROM users WHERE login = \"%s\" AND password = \"%s\"",
                login, pass);

        checkConnection();
        ResultSet select = statement.executeQuery(sqlCommand);
        return select.next();

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
