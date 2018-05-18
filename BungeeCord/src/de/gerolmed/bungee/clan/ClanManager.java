/*
 * Copyright (c) 2018.
 * This content has been created by Gerolmed! Sharing this content without permission is not aloud, as well as modifying any code parts and claiming ownership!
 */

package de.gerolmed.bungee.clan;

import de.gerolmed.bungee.clan.utils.MySQLConnectionClans;
import de.gerolmed.lib.clan.Clan;

import java.util.ArrayList;

public class ClanManager {

    private ArrayList<Clan> clans = new ArrayList<>();


    public ClanManager() {
        loadClans();
    }

    public void addClan(Clan clan) {
        clans.add(clan);
    }

    public void removeClan(Clan clan) {
        clan.remove();
        clans.remove(clan);
    }

    private void loadClans() {
        MySQLConnectionClans clanSql = new MySQLConnectionClans();
        clanSql.connect();
        clans = (ArrayList<Clan>) clanSql.getAllClans();
        clanSql.disconnect();
    }

    public void saveClans() {
        MySQLConnectionClans clanSql = new MySQLConnectionClans();
        clanSql.connect();
        clanSql.setAllClans(clans);
        clanSql.disconnect();
    }

    public Clan getClan(String clanShort) {
        for(Clan clan : clans) {
            if(clan.getShort().equals(clanShort))
                return clan;
        }

        return null;
    }
}
