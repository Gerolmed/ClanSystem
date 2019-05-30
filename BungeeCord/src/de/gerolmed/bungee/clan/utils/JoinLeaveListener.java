/*
 * Copyright (c) 2018.
 * This content has been created by Gerolmed! Sharing this content without permission is not allowed, as well
 * as modifying any code parts and claiming ownership!
 */

package de.gerolmed.bungee.clan.utils;

import de.gerolmed.bungee.clan.BungeeClan;
import de.gerolmed.lib.clan.ClanUser;
import de.gerolmed.lib.clan.ClanUserManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.TimeUnit;

public class JoinLeaveListener implements Listener {

    public JoinLeaveListener(BungeeClan bungeeClan) {
        ProxyServer.getInstance().getPluginManager().registerListener(bungeeClan, this);
    }

    @EventHandler
    public void connect(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();

        ClanUserManager.getInstance().addUserNoReplace(new ClanUser(player.getUniqueId(), player.getDisplayName()));
        //sendUpdate(player);

    }

    @EventHandler
    public void serverSwitchEvent(ServerSwitchEvent event) {
        ProxiedPlayer player = event.getPlayer();
        sendUpdate(player);
    }

    private void sendUpdate(ProxiedPlayer player) {
        ProxyServer.getInstance().getScheduler().schedule(BungeeClan.getInstance(), new Runnable() {
            @Override
            public void run() {
                ClanUser user = ClanUserManager.getInstance().getUser(player.getUniqueId());
                ClanCommand.sendUpdateToServer(user);
            }
        }, 1, TimeUnit.SECONDS);
    }
}
