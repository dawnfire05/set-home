package org.setHome.setHome.repository;

import com.google.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.setHome.setHome.model.Home;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HomeRepositoryImpl implements HomeRepository {

    private final Connection connection;

    @Inject
    public HomeRepositoryImpl(Connection connection) throws SQLException {
        this.connection = connection;
        createTables();
    }

    private void createTables() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS homes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "player_uuid TEXT NOT NULL," +
                "home_name TEXT NOT NULL," +
                "world TEXT NOT NULL," +
                "x REAL NOT NULL," +
                "y REAL NOT NULL," +
                "z REAL NOT NULL," +
                "UNIQUE(player_uuid, home_name)" + // 집 이름 중복 방지
                ");";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }

    @Override
    public List<Home> getHomes(UUID playerUUID) {
        List<Home> homes = new ArrayList<>();
        String query = "SELECT * FROM homes WHERE player_uuid = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, playerUUID.toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String homeName = rs.getString("home_name");
                String world = rs.getString("world");
                double x = rs.getDouble("x");
                double y = rs.getDouble("y");
                double z = rs.getDouble("z");
                Location location = new Location(Bukkit.getWorld(world), x, y, z);
                homes.add(new Home(homeName, location));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return homes;
    }

    @Override
    public void addHome(UUID playerUUID, Home home) {
        String insertSQL = "INSERT INTO homes (player_uuid, home_name, world, x, y, z) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, playerUUID.toString());
            pstmt.setString(2, home.getName());
            pstmt.setString(3, home.getLocation().getWorld().getName());
            pstmt.setDouble(4, home.getLocation().getX());
            pstmt.setDouble(5, home.getLocation().getY());
            pstmt.setDouble(6, home.getLocation().getZ());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateName(UUID playerUUID, Home home, String newName) {
        String updateSQL = "UPDATE homes SET home_name = ? WHERE player_uuid = ? AND home_name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)){
            pstmt.setString(1, newName);
            pstmt.setString(2, playerUUID.toString());
            pstmt.setString(3, home.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeHome(UUID playerUUID, String homeName) {
        String deleteSQL = "DELETE FROM homes WHERE player_uuid = ? AND home_name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setString(1, playerUUID.toString());
            pstmt.setString(2, homeName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Home getHome(UUID playerUUID, String homeName) {
        String query = "SELECT * FROM homes WHERE player_uuid = ? AND home_name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, playerUUID.toString());
            pstmt.setString(2, homeName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String world = rs.getString("world");
                double x = rs.getDouble("x");
                double y = rs.getDouble("y");
                double z = rs.getDouble("z");
                Location location = new Location(Bukkit.getWorld(world), x, y, z);
                return new Home(homeName, location);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isHomeNameTaken(UUID playerUUID, String homeName) {
        String query = "SELECT COUNT(*) AS count FROM homes WHERE player_uuid = ? AND home_name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, playerUUID.toString());
            pstmt.setString(2, homeName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}