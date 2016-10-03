package com.matthewedevelopment.bans;
/* 
 * Copyright © 2016 Matthew E Development - All Rights Reserved
 * You may NOT use, distribute and modify this code.
 */

import com.matthewedevelopment.bans.commands.CommandBan;
import com.matthewedevelopment.bans.commands.CommandKick;
import com.matthewedevelopment.bans.database.ConnectionPool;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

/**
 * Created by Matthew E on 10/3/2016 at 4:32 PM.
 */
public class LegendWarsBans extends Plugin {

    private static LegendWarsBans instance;

    public static LegendWarsBans getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        ConnectionPool.getConnection();
        registerCommands();
    }

    private void registerCommands() {
        PluginManager pm = getProxy().getPluginManager();
        pm.registerCommand(this, new CommandKick("kick"));
        pm.registerCommand(this, new CommandBan("ban"));
    }
}
