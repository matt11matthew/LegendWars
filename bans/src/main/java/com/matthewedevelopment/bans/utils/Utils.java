package com.matthewedevelopment.bans.utils;
/* 
 * Copyright © 2016 Matthew E Development - All Rights Reserved
 * You may NOT use, distribute and modify this code.
 */

import net.md_5.bungee.api.ChatColor;

/**
 * Created by Matthew E on 10/3/2016 at 5:46 PM.
 */
public class Utils {

    public static String colorCodes(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
