package com.celcius.religions.storage;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL extends DatabaseManager{
    @Override
    public void setup(String host, String database, String username, String password) {
        try {

            if (conn != null && !(conn.isClosed())) return;


            synchronized (this) {

                Class.forName("com.mysql.cj.jdbc.Driver");

                conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + 3306 + "/" + database, username, password);

                createTables();

            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
