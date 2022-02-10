package com.celcius.religions.storage;
import com.celcius.religions.Religions;

import java.sql.*;

public class SQLite extends DatabaseManager {

    private final Religions plugin = Religions.getPlugin(Religions.class);
    @Override
    public void setup(String host, String database, String username, String password) {
        try {
            synchronized (this) {

                Class.forName("org.sqlite.JDBC");

                conn = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder().toPath() + "/database.db");

                createTables();

            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}