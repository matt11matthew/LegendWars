package com.matthewedevelopment.bans.listeners;
/* 
 * Copyright © 2016 Matthew E Development - All Rights Reserved
 * You may NOT use, distribute and modify this code.
 */

import com.matthewedevelopment.bans.utils.LegendWarsBanAPI;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Created by Matthew E on 10/4/2016 at 5:17 PM.
 */
public class PlayerListeners implements Listener {

    @EventHandler
    public void onConnect(ServerConnectEvent e) {
        ProxiedPlayer player = e.getPlayer();
        if (LegendWarsBanAPI.isBanned(player)) {
            player.disconnect(LegendWarsBanAPI.getCancelMessage(player));
            e.setCancelled(true);
            return;
        }
    }
}
