package com.geekbrains.server.authorization;

import java.sql.*;

public class DbAuthService implements AuthService {
    private static Connection connection;
    private static Statement statement;


    public void connection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("can't connect to database");
        }
    }

    @Override
    public void start() {

    }

    @Override
    public String getNickNameByLoginAndPassword(String login, String password) {
        try (ResultSet resultSet = statement.executeQuery("select nickname from users where password = '" + password + "';")) {
            if (resultSet.next()) {
                return resultSet.getString("nickname");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void end() {

    }

    public void disconnect() {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

}
