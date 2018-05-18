/*
 * Copyright (c) 2018.
 * This content has been created by Gerolmed! Sharing this content without permission is not aloud, as well as modifying any code parts and claiming ownership!
 */

package de.gerolmed.bungee.clan;

import de.gerolmed.bungee.clan.utils.ClanCommand;
import de.gerolmed.lib.clan.ClanUser;
import de.gerolmed.lib.clan.ClanUserManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class ChannelListener implements Listener {

    public ChannelListener(BungeeClan bungeeClan) {
        ProxyServer.getInstance().getPluginManager().registerListener(bungeeClan, this);
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));


        if (event.getTag().equalsIgnoreCase("BungeeCord")) {
            try {
                String channel = in.readUTF();
                String input = in.readUTF();

                if(channel.equalsIgnoreCase("clan"))
                {
                    //Currently Spigot has nothing to say :P
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
