package de.gerolmed.spigot.clan;

import de.gerolmed.lib.clan.Clan;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ClanListener implements Listener {

    private SpigotClan spigotClan;

    public ClanListener(SpigotClan spigotClan) {
        this.spigotClan = spigotClan;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void changeClan(ClanUpdateEvent event) {
        String prefix = "";
        Clan clan = event.getClanUser().getClan();

        if (clan != null) {
            prefix = clan.getShort();
        }

        spigotClan.getChat().setPlayerPrefix(event.getPlayer(), prefix);
    }

}
