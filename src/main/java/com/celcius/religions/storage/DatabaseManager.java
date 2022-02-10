package com.celcius.religions.storage;

import com.celcius.religions.Religions;
import com.celcius.religions.object.PlayerAndReligion;
import com.celcius.religions.object.Religion;
import com.celcius.religions.utils.Chat;
import com.celcius.religions.utils.SetCommand;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class DatabaseManager {

    private final Religions plugin = Religions.getPlugin(Religions.class);
    private final Chat chat = plugin.getChat();

    Connection conn;

    public Connection getConnection() {
        return this.conn;
    }

    abstract void setup(String host, String database, String username, String password);

    public void createTables() throws SQLException {
        try {
            PreparedStatement stmt = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_data(uuid VARCHAR(50), name VARCHAR(50), religion VARCHAR(50), points INT unsigned CHECK (points >= 0))");
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean removePlayerFromReligion(Player p){
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM player_data where uuid = ?");
            statement.setString(1, String.valueOf(p.getUniqueId()));
            statement.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public boolean insertPlayerToReligion(Player p, Religion religion){
        try {
            PreparedStatement statement = conn.prepareStatement("REPLACE INTO player_data (uuid,name,religion,points) VALUES(?,?,?,?)");
            statement.setString(1, String.valueOf(p.getUniqueId()));
            statement.setString(2, p.getName());
            statement.setString(3, religion.getId());
            statement.setInt(4, 0);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public PlayerAndReligion getPlayerInReligion(OfflinePlayer p){
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM player_data WHERE uuid = ?");
            statement.setString(1, String.valueOf(p.getUniqueId()));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String uuid =resultSet.getString("uuid");
                String name =resultSet.getString("name");
                String religion=resultSet.getString("religion");
                int points=resultSet.getInt("points");
                PlayerAndReligion playerAndReligion = new PlayerAndReligion(uuid, name, religion, points);
                return playerAndReligion;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public boolean verifyPlayerInReligion(Player p) {
        String uuidFromSql = null;
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM player_data WHERE uuid = ?");
            statement.setString(1, String.valueOf(p.getUniqueId()));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                uuidFromSql = resultSet.getString("uuid");
            }
            if (uuidFromSql != null) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean givePointsToAllPlayersByReligion(String religionId, int points){
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE `player_data` SET `points`= `points` + ? WHERE `religion` = ? AND points >= 0");
            statement.setInt(1, points);
            statement.setString(2, String.valueOf(religionId));
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

    public boolean givePointsToPlayers(UUID uuid, int points){
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE `player_data` SET `points`= `points` + ? WHERE `uuid` = ? AND points >= 0");
            statement.setInt(1, points);
            statement.setString(2, String.valueOf(uuid));
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

    public boolean removePointsToAllPlayersByReligion(String religionId, int points){
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE `player_data` SET `points`= `points` - ? WHERE `religion` = ? AND points >= 0");
            statement.setInt(1, points);
            statement.setString(2, String.valueOf(religionId));
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

    public boolean removePointsToPlayers(UUID uuid, int points){
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE `player_data` SET `points`= `points` - ? WHERE `uuid` = ? AND points >= 0");
            statement.setInt(1, points);
            statement.setString(2, String.valueOf(uuid));
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

    public double getPointsFromReligions(String religionId){
        double points = 0;
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT sum(points) as points FROM player_data WHERE religion = ?");
            statement.setString(1, religionId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                points = resultSet.getDouble("points");
            }
            return points;
        } catch (SQLException e) {
            e.printStackTrace();
            return points;
        }
    }

    public int getNumberOfPlayerInReligion(String religionId){
        int count = 0;
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT COUNT(*) as count FROM player_data WHERE religion = ?");
            statement.setString(1, religionId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                count = resultSet.getInt("count");
            }
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            return count;
        }
    }

    public List<UUID> getPlayersFromReligion(String religionId){
        List<UUID> uuid = new ArrayList<>();
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT uuid FROM player_data WHERE religion = ?");
            statement.setString(1, religionId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                UUID identificador = UUID.fromString(resultSet.getString("UUID"));
                uuid.add(identificador);
            }
            return uuid;
        } catch (SQLException e) {
            e.printStackTrace();
            return uuid;
        }
    }
}

