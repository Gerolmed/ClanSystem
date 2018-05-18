/*
 * Copyright (c) 2018.
 * This content has been created by Gerolmed! Sharing this content without permission is not aloud, as well as modifying any code parts and claiming ownership!
 */

package de.gerolmed.spigot.clan;

import de.gerolmed.lib.clan.ClanUser;
import de.gerolmed.lib.clan.ClanUserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class PluginChannelListener implements PluginMessageListener {

	@Override
	public synchronized void onPluginMessageReceived(String channel, Player player, byte[] message) {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
		try {
			String subchannelRaw = in.readUTF();
			UUID uuid = UUID.fromString(subchannelRaw.split("/")[0]);
			String subchannel = subchannelRaw.split("/")[1];
			String input = in.readUTF();

			player = Bukkit.getPlayer(uuid);

			if (subchannel.equals("clanupdate")) {
				ClanUser clanUser = ClanUser.serializeFromBukkit(uuid, input);
				ClanUserManager.getInstance().addUser(clanUser);

				ClanUpdateEvent clanUpdateEvent = new ClanUpdateEvent(player, clanUser);
				Bukkit.getPluginManager().callEvent(clanUpdateEvent);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}