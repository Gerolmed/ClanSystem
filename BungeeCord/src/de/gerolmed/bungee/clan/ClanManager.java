/*
 * Copyright (c) 2018.
 * This content has been created by Gerolmed! Sharing this content without permission is not allowed, as well
 * as modifying any code parts and claiming ownership!
 */

package de.gerolmed.bungee.clan;

import de.gerolmed.bungee.clan.utils.ClanBackend;
import de.gerolmed.bungee.clan.utils.MySQLConnectionClans;
import de.gerolmed.lib.clan.Clan;

import java.util.ArrayList;

public class ClanManager {

    private ArrayList<Clan> clans = new ArrayList<>();

    private String host;
    private int port;
    private String database;
    private String username;
    private String password;
    private String table;

    public ClanManager(String host, int port, String database, String username, String password, String table) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.table = table;
    }

    public void addClan(Clan clan) {
        clans.add(clan);
    }

    public void removeClan(Clan clan) {
        clan.remove();
        clans.remove(clan);
    }

    private void loadClans() {
        ClanBackend clanBackend = new MySQLConnectionClans(host, port, database, username, password, table);
        clanBackend.connect();
        clans = (ArrayList<Clan>) clanBackend.getAllClans();
        clanBackend.disconnect();
    }

    public void saveClans() {
        ClanBackend clanBackend = new MySQLConnectionClans(host, port, database, username, password, table);
        clanBackend.connect();
        clanBackend.setAllClans(clans);
        clanBackend.disconnect();
    }

    public Clan getClan(String clanShort) {
        for(Clan clan : clans) {
            if(clan.getShort().equals(clanShort))
                return clan;
        }

        return null;
    }
}
