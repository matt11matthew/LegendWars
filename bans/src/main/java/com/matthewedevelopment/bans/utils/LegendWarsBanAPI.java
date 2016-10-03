package com.matthewedevelopment.bans.utils;
/* 
 * Copyright © 2016 Matthew E Development - All Rights Reserved
 * You may NOT use, distribute and modify this code.
 */

import com.matthewedevelopment.bans.LegendWarsBans;
import com.matthewedevelopment.bans.database.ConnectionPool;
import com.matthewedevelopment.bans.exceptions.CannotFindUUIDException;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



/**
 * Created by Matthew E on 10/3/2016 at 4:57 PM.
 */
public class LegendWarsBanAPI {

    private static final File CONFIG_FILE = new File(LegendWarsBans.getInstance().getDataFolder() + "", "config.json");

    public static void ban(String name, String reason, String who) throws CannotFindUUIDException {
        String uuid = UUIDUtils.getUUID(name);
        if (uuid == null) {
            throw new CannotFindUUIDException();
        }
        PreparedStatement pst = null;
        try {
            pst = ConnectionPool.getConnection().prepareStatement("update bans set who=" +who + " where id=" + uuid + ";");
            pst.execute();
            pst = ConnectionPool.getConnection().prepareStatement("update bans set time=-1 where id=" + uuid + ";");
            pst.execute();
            pst = ConnectionPool.getConnection().prepareStatement("update bans set reason="+reason+" where id=" + uuid + ";");
            pst.execute();
            if (LegendWarsBans.getInstance().getProxy().getPlayer(name) != null) {
                ProxiedPlayer player = LegendWarsBans.getInstance().getProxy().getPlayer(name);
                JSONObject obj = JSONUtils.getJSONObject(CONFIG_FILE).getJSONObject("message").getJSONObject("ban");
                String banReason = (reason == null) ? obj.getString("defaultReason") : reason;
                player.disconnect(new TextComponent(Utils.colorCodes(banReason)));
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void unban(String name, String reason, String who) throws CannotFindUUIDException {
        String uuid = UUIDUtils.getUUID(name);
        if (uuid == null) {
            throw new CannotFindUUIDException();
        }
        PreparedStatement pst = null;
        try {
            pst = ConnectionPool.getConnection().prepareStatement("update bans set who=" +who + " where id=" + uuid + ";");
            pst.execute();
            pst = ConnectionPool.getConnection().prepareStatement("update bans set time=0 where id=" + uuid + ";");
            pst.execute();
            pst = ConnectionPool.getConnection().prepareStatement("update bans set unbanreason="+reason+" where id=" + uuid + ";");
            pst.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void kick(ProxiedPlayer player, String reason, String who) {
       try {
           JSONObject obj = JSONUtils.getJSONObject(CONFIG_FILE).getJSONObject("message").getJSONObject("kick");
           String message = obj.getString("kickMessage");
           reason = (reason == null) ? obj.getString("defaultReaon") : reason;
           message = message.replaceAll("%server%", player.getServer().getInfo().getName());
           message = message.replaceAll("%kicker%", who);
           message = message.replaceAll("%player%", player.getName());
           message = message.replaceAll("%reason%", reason);
           message = message.replaceAll("%nl%", "\n");
           player.disconnect(new TextComponent(Utils.colorCodes(reason)));
           JSONObject bc = obj.getJSONObject("broadcast");
           if (bc.getBoolean("enable")) {
               String msg = bc.getString("message");
               msg = msg.replaceAll("%server%", player.getServer().getInfo().getName());
               msg = msg.replaceAll("%kicker%", who);
               msg = msg.replaceAll("%player%", player.getName());
               msg = msg.replaceAll("%reason%", reason);
               msg = msg.replaceAll("%nl%", "\n");
               ProxyServer server = LegendWarsBans.getInstance().getProxy();
               server.broadcast(new TextComponent(Utils.colorCodes(msg)));
           }
       } catch (JSONException e) {
           e.printStackTrace();
       }
    }

    public static boolean isBanned(String id) {
        PreparedStatement pst = null;
        boolean ban = true;
        try {
            pst = ConnectionPool.getConnection().prepareStatement("select * from bans where id = " + id + ";");
            pst.executeUpdate();
            ResultSet rs = pst.getResultSet();
            if (rs.next()) {
                ban = (rs.getLong("time") > 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ban;
    }

    public static boolean hasPermission(ProxiedPlayer player, String permission) {
        return true;//TODO finish method
    }
}
