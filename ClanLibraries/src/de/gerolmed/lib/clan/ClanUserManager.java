/*
 * Copyright (c) 2018.
 * This content has been created by Gerolmed! Sharing this content without permission is not allowed, as well
 * as modifying any code parts and claiming ownership!
 */

package de.gerolmed.lib.clan;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClanUserManager {

    //Decleration of static instnances - Start

    private static ClanUserManager instance;

    public static ClanUserManager getInstance() {
        return instance;
    }

    //Decleration of static instnances - END


    private ArrayList<ClanUser> users;

    public ClanUserManager() {
        instance = this;
        users = new ArrayList<>();
    }

    public UUID getIdFromName(String name) {

        for(ClanUser user : users)
        if(user.getNameString().equalsIgnoreCase(name))
            return user.getUUID();
        return null;

    }

    public void addUser(ClanUser user) {
        if(isUser(user.getUUID()))
            removeUser(user.getUUID());

        users.add(user);

        callUpdate(user);
    }

    private void removeUser(UUID uuid) {
        ClanUser targetUser = null;
        for(ClanUser user : users)
            if(user.isUser(uuid)) {
                targetUser = user;
                break;
        }

        users.remove(targetUser);

    }

    private boolean isUser(UUID uuid) {
        for(ClanUser user : users)
            if(user.isUser(uuid))
                return true;
        return false;
    }

    private void callUpdate(ClanUser user) {
        //TODO: Send Update event to notice all plugins
    }

    public ClanUser getUser(UUID uuid) {
        for(ClanUser user : users)
            if(user.isUser(uuid)) {
                return user;
            }
        return null;
    }

    public void addUserNoReplace(ClanUser user) {
        if(!isUser(user.getUUID()))
            addUser(user);
    }

    /**
     * This will only work correctly on BungeeSide
     * SpigotSide isn't the complete list and might be outdated
     * @return
     */
    public List<ClanUser> getAllUsers() {
        return users;
    }

    public void setAllUsers(ArrayList<ClanUser> allUsers) {
        users = allUsers;
    }
}
