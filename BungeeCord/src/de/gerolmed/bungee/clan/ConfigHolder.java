/*
 * Copyright (c) 2018.
 * This content has been created by Gerolmed! Sharing this content without permission is not allowed, as well
 * as modifying any code parts and claiming ownership!
 */

package de.gerolmed.bungee.clan;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;

public class ConfigHolder {

    private BungeeClan plugin;

    public ConfigHolder(BungeeClan plugin) {
        this.plugin = plugin;
        createConfigs();
    }

    private void createConfigs() {
        for(Configs config : Configs.values()) {
            config.init(this);
        }
    }

    public Configuration createConfig(String name) {
        File conf = new File(plugin.getDataFolder(), name);

        if(!conf.exists()) {
            conf.getParentFile().mkdirs();
        }

        Configuration configRet = null;

        try {
            configRet = ConfigurationProvider.getProvider(YamlConfiguration.class).load(conf);
            return configRet;
        } catch (Exception  ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public void saveConfig(Configuration config, String name) {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(plugin.getDataFolder(), name));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum Configs {
        CONFIG("config.yml");

        private String name;
        private Configuration config;

        private Configs(String name) {
            this.name = name;
        }

        public void init(ConfigHolder holder) {
            this.config = holder.createConfig(name);
        }

        public Configuration getConfig() {
            return config;
        }

        public void saveConfig() {
            BungeeClan.getInstance().getConfigHolder().saveConfig(config, name);
        }
    }

}
