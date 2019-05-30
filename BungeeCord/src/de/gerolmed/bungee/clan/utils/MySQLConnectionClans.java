/*
 * Copyright (c) 2018.
 * This content has been created by Gerolmed! Sharing this content without permission is not allowed, as well
 * as modifying any code parts and claiming ownership!
 */

package de.gerolmed.bungee.clan.utils;

import de.gerolmed.bungee.clan.BungeeClan;
import de.gerolmed.bungee.clan.ClanManager;
import de.gerolmed.bungee.clan.ConfigHolder;
import de.gerolmed.lib.clan.Clan;
import de.gerolmed.lib.clan.ClanUser;
import de.gerolmed.lib.clan.utils.ClanRank;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MySQLConnectionClans {

    private String host = ConfigHolder.Configs.CONFIG.getConfig().getString("MySQL.host");
    private int port = ConfigHolder.Configs.CONFIG.getConfig().getInt("MySQL.port");
    private String database = ConfigHolder.Configs.CONFIG.getConfig().getString("MySQL.database");
    private String username = ConfigHolder.Configs.CONFIG.getConfig().getString("MySQL.username");
    private String password = ConfigHolder.Configs.CONFIG.getConfig().getString("MySQL.password");
    private Connection connection;

    private String table = "Clans";

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
                        "CREATE TABLE IF NOT EXISTS "+table+" (ClanName VARCHAR(100), ClanShort VARCHAR(100))");
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

    public List<Clan> getAllClans() {
        System.out.println("Attempting to fetch Clans from Database");

        ArrayList<Clan> clans = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("SELECT * FROM "+table);

            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                String clanName = result.getString("ClanName");
                String clanShort = result.getString("ClanShort");


                try {
                    Clan clan = new Clan(clanShort, clanName);
                    clans.add(clan);
                    System.err.println("  -> Loaded Clan "+clan.getName() + " ("+clan.getShort()+")");

                }catch (Exception ex) {
                    System.err.println("Error loading Clan from database");
                    ex.printStackTrace();
                }
            }
            result.close();
            preparedStatement.close();
            System.out.println("Fetched Clans from Database");
        } catch (Exception ex) {
            System.out.println("Failed to fetch Clans from Database");
        }
        return clans;
    }

    public void setAllClans(List<Clan> clans) {
        System.out.println("Attempting to fetch ClanUsers from Database");

        cleanDatabase();

        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("INSERT INTO "+table+" (ClanName, ClanShort) VALUES (?, ?)");

            for(Clan clan : clans) {
                String clanName = clan.getName();
                String clanShort = clan.getShort();

                preparedStatement.setString(1, clanName);
                preparedStatement.setString(2, clanShort);

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
