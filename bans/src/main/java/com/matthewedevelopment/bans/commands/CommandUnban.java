package com.matthewedevelopment.bans.commands;
/* 
 * Copyright © 2016 Matthew E Development - All Rights Reserved
 * You may NOT use, distribute and modify this code.
 */

import com.matthewedevelopment.bans.exceptions.CannotFindUUIDException;
import com.matthewedevelopment.bans.utils.LegendWarsBanAPI;
import com.matthewedevelopment.bans.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/**
 * Created by Matthew E on 10/3/2016 at 5:53 PM.
 */
public class CommandUnban extends Command {
    public CommandUnban(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            if (!LegendWarsBanAPI.canBan((ProxiedPlayer) sender)) {
                sender.sendMessage(Utils.colorCodes("&cYou don't have permission"));
                return;
            }
           if (args.length == 1) {
               String name = args[0];
               try {
                   LegendWarsBanAPI.unban(name, null, sender.getName());
               } catch (CannotFindUUIDException e) {
                   sender.sendMessage(Utils.colorCodes("&cCouldn't fetch the uuid of " + name + " please wait 1 minute"));
                   return;
               }
               sender.sendMessage(Utils.colorCodes("&aYou have unbanned " + name));
               return;
           }
        }
    }
}