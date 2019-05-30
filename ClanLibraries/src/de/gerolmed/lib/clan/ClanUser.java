/*
 * Copyright (c) 2018.
 * This content has been created by Gerolmed! Sharing this content without permission is not allowed, as well
 * as modifying any code parts and claiming ownership!
 */

package de.gerolmed.lib.clan;

import de.gerolmed.lib.clan.utils.BungeeSided;
import de.gerolmed.lib.clan.utils.ClanRank;

import java.util.UUID;

public class ClanUser {

    private UUID uuid;
    private Clan clan;
    private ClanRank rank;
    private String nameString;

    public ClanUser(UUID uuid, String nameString) {
        if(uuid == null)
            throw new NullPointerException();
        this.uuid = uuid;
        this.nameString = nameString;
    }

    private ClanUser(UUID uuid, ClanRank rank, Clan clan) {
        this.uuid = uuid;
        this.rank = rank;
        this.clan = clan;
    }

    public boolean isUser(UUID uuid) {
        return this.uuid.equals(uuid);
    }

    public Clan getClan() {
        return clan;
    }

    /**
     * WARNING: only to be used on bungee side
     * You will still need to update spigot side!!
     * @param clan - new clan
     */
    @BungeeSided
    public void setClan(Clan clan) {
        this.clan = clan;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setRank(ClanRank rank) {
        this.rank = rank;
    }

    public ClanRank getRank() {
        return rank;
    }

    public String serializeToBukkit() {
        String clanInfo = clan != null ? clan.serializeToBukkit() : "";
        String clanRank = rank == null ? "none" : rank.toString();
        return "rank:"+ clanRank +"-" + clanInfo;
    }

    public static ClanUser serializeFromBukkit(UUID uuid, String string) {
        ClanRank clanRank = null;
        Clan clan = null;

        try {
            if(string != null) {
                String[] splitString = string.split("-");
                {
                    String playerData = splitString[0];
                    for(String part : playerData.split("/"))
                    {
                        String[] keyValPair = part.split(":");
                        String key = keyValPair[0];
                        String val = keyValPair[1];

                        if(key.equalsIgnoreCase("rank"))
                            clanRank = ClanRank.findVal(val);
                    }

                    if(splitString.length > 1 && splitString[1] != null)
                    {
                        clan = Clan.serializeFromBukkit(splitString[1]);
                    }
                }
            }
        } catch (Exception ex) {}

        return new ClanUser(uuid, clanRank, clan);
    }

    @BungeeSided
    public String getNameString() {
        return nameString;
    }
}
