/*
 * Copyright (c) 2018.
 * This content has been created by Gerolmed! Sharing this content without permission is not allowed, as well
 * as modifying any code parts and claiming ownership!
 */

package de.gerolmed.bungee.clan.utils;

import de.gerolmed.lib.clan.Clan;

import java.util.List;

public interface ClanBackend {
    void connect();
    void disconnect();
    List<Clan> getAllClans();
    void setAllClans(List<Clan> clans);
}
