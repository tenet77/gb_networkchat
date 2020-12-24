package server;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static DBConnection instance;

    private final String USER = "java";
    private final String PASS = "!1qaz@2wsx";
    private final String DBNAME = "java";
    private final Connection connection;

    public static DBConnection getInstance() throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (instance == null) {
            instance = new DBConnection();
        }

        return instance;
    }

    public DBConnection() throws SQLException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        connection = DriverManager.getConnection("jdbc:mysql://localhost/"+DBNAME+"?serverTimezone=Europe/Moscow&useSSL=false",
                USER, PASS);
    }

    public boolean addUser(String login, String pass, String nick) throws SQLException {
        String sqlCommand = String.format("INSERT INTO users (login, password, nick) VALUES(\"%s\",\"%s\",\"%s\")",
                login, pass, nick);

        Statement statement = connection.createStatement();
        int rows = statement.executeUpdate(sqlCommand);
        if (rows > 0) return true;

        return false;
    }
}
