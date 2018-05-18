package de.gerolmed.bungee.clan;

import de.gerolmed.bungee.clan.utils.ClanCommand;
import de.gerolmed.bungee.clan.utils.JoinLeaveListener;
import de.gerolmed.bungee.clan.utils.MySQLConnectionClans;
import de.gerolmed.bungee.clan.utils.MySQLConnectionUsers;
import de.gerolmed.lib.clan.ClanUser;
import de.gerolmed.lib.clan.ClanUserManager;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;

public class BungeeClan extends Plugin {

    private ClanManager clanManager;
    private ConfigHolder configHolder;

    private static BungeeClan instance;

    public static BungeeClan getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        try {
            configHolder = new ConfigHolder(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        clanManager = new ClanManager();
        new ClanUserManager();

        MySQLConnectionUsers userSql = new MySQLConnectionUsers();
        userSql.connect();
        ClanUserManager.getInstance().setAllUsers((ArrayList<ClanUser>) userSql.getAllUsers(clanManager));
        userSql.disconnect();

        new ChannelListener(this);
        new JoinLeaveListener(this);

        new ClanCommand(this);
    }

    @Override
    public void onDisable() {

        clanManager.saveClans();
        MySQLConnectionUsers userSql = new MySQLConnectionUsers();
        userSql.connect();
        userSql.setAllUsers(ClanUserManager.getInstance().getAllUsers());
        userSql.disconnect();
    }

    public ClanManager getClanManager() {
        return clanManager;
    }

    public ConfigHolder getConfigHolder() {
        return configHolder;
    }
}
