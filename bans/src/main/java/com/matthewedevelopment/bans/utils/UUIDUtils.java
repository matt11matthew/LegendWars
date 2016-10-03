package com.matthewedevelopment.bans.utils;
/* 
 * Copyright © 2016 Matthew E Development - All Rights Reserved
 * You may NOT use, distribute and modify this code.
 */

import com.matthewedevelopment.bans.mojang.UUIDFetcher;

/**
 * Created by Matthew E on 10/3/2016 at 5:22 PM.
 */
public class UUIDUtils {

    public static String getUUID(String name) {
        try {
            return UUIDFetcher.getUUIDOf(name).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
