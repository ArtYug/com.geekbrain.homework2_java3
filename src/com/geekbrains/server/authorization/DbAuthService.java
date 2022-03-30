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
       // try (ResultSet resultSet = statement.executeQuery("select nickname from users where  password = '" + password + "';"))
      String query = String.format("select nickname from users where login = '%s' and password = '%s' ; ",login,password );
        try (ResultSet resultSet = statement.executeQuery(query)){
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
