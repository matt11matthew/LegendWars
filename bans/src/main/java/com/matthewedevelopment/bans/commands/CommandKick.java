package com.matthewedevelopment.bans.commands;
/* 
 * Copyright © 2016 Matthew E Development - All Rights Reserved
 * You may NOT use, distribute and modify this code.
 */

import com.matthewedevelopment.bans.LegendWarsBans;
import com.matthewedevelopment.bans.utils.LegendWarsBanAPI;
import com.matthewedevelopment.bans.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/**
 * Created by Matthew E on 10/3/2016 at 5:53 PM.
 */
public class CommandKick extends Command {
    public CommandKick(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            if (!LegendWarsBanAPI.hasPermission((ProxiedPlayer) sender, "legendwars.kick")) {
                sender.sendMessage(Utils.colorCodes("&cYou don't have permission"));
                return;
            }
        }
        if (args.length == 1) {
            String name = args[0];
            if (LegendWarsBans.getInstance().getProxy().getPlayer(name) == null) {
                sender.sendMessage(Utils.colorCodes("&c" + name + " is offline!"));
                return;
            }
            ProxiedPlayer player = LegendWarsBans.getInstance().getProxy().getPlayer(name);
            LegendWarsBanAPI.kick(player, null, sender.getName());
            sender.sendMessage(Utils.colorCodes("&aYou have kicked " + name));
            return;
        }
    }
}