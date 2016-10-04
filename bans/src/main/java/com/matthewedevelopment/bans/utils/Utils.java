package com.matthewedevelopment.bans.utils;
/* 
 * Copyright © 2016 Matthew E Development - All Rights Reserved
 * You may NOT use, distribute and modify this code.
 */

import net.md_5.bungee.api.ChatColor;

import java.util.concurrent.TimeUnit;

/**
 * Created by Matthew E on 10/3/2016 at 5:46 PM.
 */
public class Utils {

    public static String colorCodes(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static long getSecondsFromMillis(long time) {
        long sec = TimeUnit.MILLISECONDS.toSeconds(time);
        if (sec - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) > 0) {
            sec = sec - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        }
        return sec;
    }

    public static int convertStringToInt(String s) {
        int i = 0;
        try {
            i = Integer.parseInt(s);
        } catch (NumberFormatException e) {

        }
        return i;
    }

    public static String parseMilis(long l) {
        long sec = TimeUnit.MILLISECONDS.toSeconds(l);
        if (sec - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) > 0) {
            sec = sec - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        }
        long hour = 0;
        long min = 0;
        long day = 0;
        while (sec >= 60) {
            sec -= 60;
            min += 1;
        }
        while (min >= 60) {
            min -= 60;
            hour += 1;
        }
        while (hour >= 24) {
            hour -= 24;
            day += 1;
        }
        String returnString = "&a" + day + "&ld &a" + hour + "&lh &a" + min + "&lm &a" + sec + "&ls";
        return returnString;
    }

    public static boolean unban(long l) {
        return (System.currentTimeMillis() >= l);
    }

    public static long convertToMillis(int hours) {
        try {
            return System.currentTimeMillis() + TimeUnit.HOURS.toMillis(hours);
        } catch (Exception e) {
            return 0;
        }
    }

    public static long convertSecondsToMillis(int seconds) {
        try {
            return System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds);
        } catch (Exception e) {
            return 0;
        }
    }

    public static long getMillis(String s) {
        int days = Integer.parseInt(s.split("d")[0]);
        int hours = Integer.parseInt(s.split("d")[1].split("h")[0]);
        int minutes = Integer.parseInt(s.split("h")[1].split("m")[0]);
        int seconds = Integer.parseInt(s.split("m")[1].split("s")[0]);
        while (days > 0) {
            days -= 1;
            hours += 24;
        }
        while (hours > 0) {
            hours -= 1;
            minutes += 60;
        }
        while (minutes > 0) {
            minutes -= 1;
            seconds += 60;
        }
        return System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds);
    }

    public static boolean canParseTime(String s) {
        try {
            int days = Integer.parseInt(s.split("d")[0]);
            int hours = Integer.parseInt(s.split("d")[1].split("h")[0]);
            int minutes = Integer.parseInt(s.split("h")[1].split("m")[0]);
            int seconds = Integer.parseInt(s.split("m")[1].split("s")[0]);
            while (days > 0) {
                days -= 1;
                hours += 24;
            }
            while (hours > 0) {
                hours -= 1;
                minutes += 60;
            }
            while (minutes > 0) {
                minutes -= 1;
                seconds += 60;
            }
            return (seconds > 0);
        } catch (Exception e) {
            return false;
        }
    }
}
