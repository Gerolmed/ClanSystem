/*
 * Copyright (c) 2018.
 * This content has been created by Gerolmed! Sharing this content without permission is not allowed, as well
 * as modifying any code parts and claiming ownership!
 */

package de.gerolmed.bungee.clan.utils;

import de.gerolmed.bungee.clan.ClanManager;
import de.gerolmed.lib.clan.ClanUser;

import java.util.List;

public interface UserBackend {
    void connect();
    void disconnect();
    List<ClanUser> getAllUsers(ClanManager clanManager);
    void setAllUsers(List<ClanUser> users);
}
