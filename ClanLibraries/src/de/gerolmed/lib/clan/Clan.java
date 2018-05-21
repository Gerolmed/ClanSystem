/*
 * Copyright (c) 2018.
 * This content has been created by Gerolmed! Sharing this content without permission is not aloud, as well as modifying any code parts and claiming ownership!
 */

package de.gerolmed.lib.clan;

import de.gerolmed.lib.clan.utils.BungeeSided;
import de.gerolmed.lib.clan.utils.ClanRank;
import de.gerolmed.lib.clan.utils.UserNotInClanException;
import sun.security.acl.GroupImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

public class Clan {
    private String shortName;
    private String name;

    private ArrayList<UUID> invites;
    private ArrayList<ClanUser> users;

    @BungeeSided
    public Clan(String shortName, String name, ClanUser owner) {
        invites = new ArrayList<>();
        users = new ArrayList<>();

        this.shortName = shortName;
        this.name = name;

        addUser(owner);
        owner.setRank(ClanRank.ADMIN);
    }

    /**
     * This is only to be used to load data
     * @param shortName
     * @param name
     */
    @BungeeSided
    public Clan(String shortName, String name) {
        invites = new ArrayList<>();
        users = new ArrayList<>();

        this.name = name;
        this.shortName = shortName;
    }

    /**
     * WARNING: only to be used on bungee side
     * You will still need to update spigot side!!
     * @param user
     */
    @BungeeSided
    public void addUser(ClanUser user) {
        user.setClan(this);
        user.setRank(ClanRank.GUEST);
        users.add(user);
        users.remove(user.getUUID());
    }

    @BungeeSided
    public boolean promote(ClanUser user) throws Exception {
        if(user.getClan() != this)
            throw new UserNotInClanException();

        ClanRank nextRank = ClanRank.getNextRank(user.getRank());

        if(canBePromoted(user, nextRank))
        {
            user.setRank(nextRank);
            return true;
        } else {
            return false;
        }
    }

    @BungeeSided
    public boolean demote(ClanUser user) throws UserNotInClanException {
        if(user.getClan() != this)
            throw new UserNotInClanException();

        ClanRank previousRank = ClanRank.getPreviousRank(user.getRank());
        ClanRank currentRank = user.getRank();

        if(previousRank != null)
        {

            if(currentRank == ClanRank.ADMIN ) {
                if(otherRankUserCount(user) == 0) {
                    ClanUser nextAdmin = highestUserBesides(user);

                    if(nextAdmin == null)
                        return false;

                    nextAdmin.setRank(ClanRank.ADMIN);
                }
            }

            user.setRank(previousRank);
            return true;
        } else {
            return false;
        }
    }

    @BungeeSided
    private int otherRankUserCount(ClanUser user) {
        int userAmount = 0;

        for(ClanUser otherUser : users)
        {
            if(otherUser.getRank() == user.getRank() && otherUser != user)
                userAmount++;
        }

        return userAmount;
    }

    @BungeeSided
    private ClanUser highestUserBesides(ClanUser user) {
        ClanUser bestUser = null;

        for(ClanUser otherUser : users)
        {
            if(otherUser != user && (bestUser == null || bestUser.getRank().ordinal() < otherUser.getRank().ordinal()))
                bestUser = otherUser;
        }

        return bestUser;
    }

    @BungeeSided
    private boolean canBePromoted(ClanUser user, ClanRank nextRank) {

        if(nextRank == null || user == null)
            return false;

        int maxUsers = nextRank.getMaxUsers();

        if(maxUsers < 0)
            return true;

        for(ClanUser otherUser : users)
        {
            if(otherUser.getRank() == nextRank)
                maxUsers--;

            if(maxUsers <= 0)
                return false;
        }

        return true;
    }

    @BungeeSided
    public ClanUser[] getUsers() {
        return users.toArray(new ClanUser[users.size()]);
    }

    public void remove() {
        //TODO: notify all users of remove and spigot
    }

    public String getShort() {
        return shortName;
    }

    @BungeeSided
    public ClanUser[] getUsersSorted() {
        ArrayList<ClanUser> users = (ArrayList<ClanUser>) this.users.clone();
        users.sort(new Comparator<ClanUser>() {
            @Override
            public int compare(ClanUser o1, ClanUser o2) {
                if(o1.getRank().ordinal() > o2.getRank().ordinal())
                    return -1;
                if(o1.getRank().ordinal() < o2.getRank().ordinal())
                    return 1;
                return 0;
            }
        });

        return users.toArray(new ClanUser[users.size()]);

    }

    public String getName() {
        return name;
    }

    @BungeeSided
    public void addInvitation(UUID uuid) {
        invites.add(uuid);
    }
    @BungeeSided
    public void removeInvitation(UUID uuid) {
        invites.remove(uuid);
    }
    @BungeeSided
    public boolean hasInvited(UUID uuid) {
        return invites.contains(uuid);
    }

    public String serializeToBukkit() {
        return "name:"+getName()+"/"+"short:"+getShort();
    }

    public static Clan serializeFromBukkit(String string) {
        String clanName = null;
        String clanShort = null;

        try {
            if(string != null) {
                for(String part : string.split("/"))
                {
                    String[] keyValPair = part.split(":");
                    String key = keyValPair[0];
                    String val = keyValPair[1];

                    if(key.equalsIgnoreCase("name"))
                        clanName = val;
                    else if(key.equalsIgnoreCase("short"))
                        clanShort = val;
                }
            }
        } catch (Exception ex) {}

        if(clanName == null || clanShort == null)
            return null;

        return new Clan(clanShort, clanName);
    }
    @BungeeSided
    public void removeUser(ClanUser user) {

        users.remove(user);
        user.setRank(null);
        user.setClan(null);
    }
    @BungeeSided
    public void setUser(ClanUser user) {
        users.add(user);
        user.setClan(this);
    }
}
