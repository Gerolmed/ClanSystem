/*
 * Copyright (c) 2018.
 * This content has been created by Gerolmed! Sharing this content without permission is not allowed, as well
 * as modifying any code parts and claiming ownership!
 */

package de.gerolmed.spigot.clan;

import de.gerolmed.lib.clan.ClanUser;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanUpdateEvent extends Event {

    private Player player;
    private ClanUser clanUser;

    public ClanUpdateEvent(Player player, ClanUser clanUser) {
        this.player = player;
        this.clanUser = clanUser;
    }


    public Player getPlayer() {
        return player;
    }

    public ClanUser getClanUser() {
        return clanUser;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
