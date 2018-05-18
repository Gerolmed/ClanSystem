/*
 * Copyright (c) 2018.
 * This content has been created by Gerolmed! Sharing this content without permission is not aloud, as well as modifying any code parts and claiming ownership!
 */

package de.gerolmed.bungee.clan;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DataSender {
    public static void sendToBukkit(String channel, String message, ProxiedPlayer player) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeUTF(player.getUniqueId().toString() + "/" + channel);
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.getServer().getInfo().sendData("Return", stream.toByteArray());

    }
}
