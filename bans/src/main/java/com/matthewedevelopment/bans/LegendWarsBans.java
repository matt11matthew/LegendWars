package com.matthewedevelopment.bans;
/* 
 * Copyright © 2016 Matthew E Development - All Rights Reserved
 * You may NOT use, distribute and modify this code.
 */

import com.matthewedevelopment.bans.commands.CommandBan;
import com.matthewedevelopment.bans.commands.CommandKick;
import com.matthewedevelopment.bans.commands.CommandTemp;
import com.matthewedevelopment.bans.commands.CommandUnban;
import com.matthewedevelopment.bans.database.ConnectionPool;
import com.matthewedevelopment.bans.utils.LegendWarsBanAPI;
import com.matthewedevelopment.bans.utils.Utils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.concurrent.TimeUnit;

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
        getProxy().getScheduler().schedule(this, new Runnable() {
            @Override
            public void run() {
                muteTask();
            }
        }, 60, TimeUnit.SECONDS);
    }

    private void registerCommands() {
        PluginManager pm = getProxy().getPluginManager();
        pm.registerCommand(this, new CommandKick("kick"));
        pm.registerCommand(this, new CommandBan("ban"));
        pm.registerCommand(this, new CommandTemp("tempban"));
        pm.registerCommand(this, new CommandUnban("unban"));
    }

     public void muteTask() {
        for (ProxiedPlayer player : getInstance().getProxy().getPlayers()) {
            if (Utils.unban(LegendWarsBanAPI.getMuteTime(player))) {
                LegendWarsBanAPI.unmute(player, null, "Console");
            }
        }
    }
}
