/*
 * Copyright (c) 2018.
 * This content has been created by Gerolmed! Sharing this content without permission is not aloud, as well as modifying any code parts and claiming ownership!
 */

package de.gerolmed.bungee.clan.utils;

import de.gerolmed.bungee.clan.BungeeClan;
import de.gerolmed.bungee.clan.ClanManager;
import de.gerolmed.bungee.clan.ConfigHolder;
import de.gerolmed.lib.clan.Clan;
import de.gerolmed.lib.clan.ClanUser;
import de.gerolmed.lib.clan.ClanUserManager;
import de.gerolmed.lib.clan.utils.ClanRank;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MySQLConnectionUsers {

    private String host = ConfigHolder.Configs.CONFIG.getConfig().getString("MySQL.host");
    private int port = ConfigHolder.Configs.CONFIG.getConfig().getInt("MySQL.port");
    private String database = ConfigHolder.Configs.CONFIG.getConfig().getString("MySQL.database");
    private String username = ConfigHolder.Configs.CONFIG.getConfig().getString("MySQL.username");
    private String password = ConfigHolder.Configs.CONFIG.getConfig().getString("MySQL.password");
    private Connection connection;

    private String table = "ClanUsers";

    public Connection getConnection() {
        return connection;
    }

    public boolean isConnected() {
        return connection != null;
    }

    public void connect() {
        if (!isConnected()) {
            try {
                connection = DriverManager.getConnection(
                        "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username,
                        password);
                System.out.println("[ClanSystem] MySQL connected!");
            } catch (SQLException ex) {
                System.out.println("[ClanSystem] MySQL failed to connect!");
            }
        }

        if (isConnected()) {
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(
                        "CREATE TABLE IF NOT EXISTS "+table+" (UUID VARCHAR(100), Clan VARCHAR(100), ClanRank VARCHAR(100))");
                preparedStatement.executeUpdate();
                preparedStatement.close();
                System.out.println("[ClanSystem] MySQL Table created/loaded!");
            } catch (SQLException ex) {
                System.out.println("[ClanSystem] MySQL Table failed to create/load!");
            }
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
                connection = null;
                System.out.println("[ClanSystem] MySQL disconnected!");
            } catch (SQLException ex) {
                System.out.println("[ClanSystem] MySQL failed to disconnect!");
            }
        }
    }

    public List<ClanUser> getAllUsers(ClanManager clanManager) {
        System.out.println("Attempting to fetch ClanUsers from Database");

        ArrayList<ClanUser> users = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("SELECT * FROM "+table);

            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                String uuidString = result.getString("UUID");
                String clanShort = result.getString("Clan");
                String clanRankString = result.getString("ClanRank");


                try {
                    UUID uuid = UUID.fromString(uuidString);
                    Clan clan = clanManager.getClan(clanShort);
                    ClanUser user = new ClanUser(uuid);
                    if(clan != null) {
                        clan.setUser(user);
                        ClanRank rank = ClanRank.findVal(clanRankString);
                        user.setRank(rank);
                        ClanUserManager.getInstance().addUser(user);
                        System.err.println("  -> Loaded ClanUser "+uuidString + " ("+clanShort+")");

                    }
                }catch (Exception ex) {
                    System.err.println("Error loading ClanUser from database");
                    ex.printStackTrace();
                }
            }
            result.close();
            preparedStatement.close();
            System.out.println("Fetched ClanUsers from Database");
        } catch (Exception ex) {
            System.out.println("Failed to fetch ClanUsers from Database");
        }
        return users;
    }

    public void setAllUsers(List<ClanUser> users) {
        System.out.println("Attempting to fetch ClanUsers from Database");

        cleanDatabase();

        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("INSERT INTO "+table+" (UUID, Clan, ClanRank) VALUES (?, ?, ?)");

            for(ClanUser clanUser : users) {
                String uuidString = clanUser.getUUID().toString();
                String clanShort = clanUser.getClan() != null ? clanUser.getClan().getShort() : null;
                String clanRankString = clanUser.getRank() != null ? clanUser.getRank().toString() : null;

                preparedStatement.setString(1, uuidString);
                preparedStatement.setString(2, clanShort);
                preparedStatement.setString(3, clanRankString);

                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            preparedStatement.close();

            System.out.println("Saved ClanUsers to Database");
        } catch (Exception ex) {
            System.out.println("Failed to Saved ClanUsers to Database");
        }
    }

    private void cleanDatabase() {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "DROP TABLE "+table);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("[ClanSystem] MySQL Table dropped!");
        } catch (SQLException ex) {
            System.out.println("[ClanSystem] MySQL Table failed to drop!");
        }
        disconnect();
        connect();
    }
}
