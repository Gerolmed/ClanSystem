package de.gerolmed.bungee.clan;

/*
 * Copyright (c) 2018.
 * This content has been created by Gerolmed! Sharing this content without permission is not allowed, as well
 * as modifying any code parts and claiming ownership!
 */

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
    private MySQLConnectionUsers userSql;

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

        clanManager = new ClanManager(
                ConfigHolder.Configs.CONFIG.getConfig().getString("clan.host"),
                ConfigHolder.Configs.CONFIG.getConfig().getInt("clan.port"),
                ConfigHolder.Configs.CONFIG.getConfig().getString("clan.database"),
                ConfigHolder.Configs.CONFIG.getConfig().getString("clan.username"),
                ConfigHolder.Configs.CONFIG.getConfig().getString("clan.password"),
                ConfigHolder.Configs.CONFIG.getConfig().getString("clan.table")
        );
        new ClanUserManager();

        userSql = new MySQLConnectionUsers(
                ConfigHolder.Configs.CONFIG.getConfig().getString("user.host"),
                ConfigHolder.Configs.CONFIG.getConfig().getInt("user.port"),
                ConfigHolder.Configs.CONFIG.getConfig().getString("user.database"),
                ConfigHolder.Configs.CONFIG.getConfig().getString("user.username"),
                ConfigHolder.Configs.CONFIG.getConfig().getString("user.password"),
                ConfigHolder.Configs.CONFIG.getConfig().getString("user.table")
        );

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
