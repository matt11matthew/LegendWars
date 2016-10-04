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
        createPlayer(uuid, name);
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
            }
            JSONObject obj = JSONUtils.getJSONObject(CONFIG_FILE).getJSONObject("message").getJSONObject("ban");
            JSONObject bc = obj.getJSONObject("broadcast");
            if (bc.getBoolean("enable")) {
                String msg = bc.getString("message");
                msg = msg.replaceAll("%banner%", who);
                msg = msg.replaceAll("%player%", name);
                msg = msg.replaceAll("%reason%", reason);
                msg = msg.replaceAll("%nl%", "\n");
                ProxyServer server = LegendWarsBans.getInstance().getProxy();
                server.broadcast(new TextComponent(Utils.colorCodes(msg)));
            }
            return;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getCancelMessage(ProxiedPlayer player) {
        try {
            JSONObject obj = JSONUtils.getJSONObject(CONFIG_FILE).getJSONObject("message");
            if (getBanTime(player) == -1) {
                JSONObject perm = obj.getJSONObject("ban");
                String msg = perm.getString("loginMessage");
                msg = msg.replaceAll("%reason%", getBanReason(player));
                msg = msg.replaceAll("%nl%", "\n");
                return Utils.colorCodes(msg);
            } else {
                JSONObject temp = obj.getJSONObject("ban");
                String msg = temp.getString("loginMessage");
                msg = msg.replaceAll("%reason%", getBanReason(player));
                msg = msg.replaceAll("%nl%", "\n");
                msg = msg.replaceAll("%time%", Utils.parseMilis(getBanTime(player)));
                return Utils.colorCodes(msg);
            }
        } catch (Exception e) {
            return "&cError";
        }
    }

    private static String getBanReason(ProxiedPlayer player) {
        String reason = null;
        try {
            reason = getString(player, "reason");
            JSONObject obj = JSONUtils.getJSONObject(CONFIG_FILE).getJSONObject("message").getJSONObject("ban");
            reason = (reason == null) ? obj.getString("defaultReason") : reason;
        } catch (Exception e) {

        }
        return Utils.colorCodes(reason);
    }

    private static String getString(ProxiedPlayer player, String reason) {
        PreparedStatement pst = null;
        try {
            pst = ConnectionPool.getConnection().prepareStatement("select * from bans where id = " + player.getUniqueId().toString() + ";");
            pst.executeUpdate();
            ResultSet rs = pst.getResultSet();
            if (rs.next()) {
                return rs.getString(reason);
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    public static void tempban(String name, String reason, String who, long time) throws CannotFindUUIDException {
        String uuid = UUIDUtils.getUUID(name);
        if (uuid == null) {
            throw new CannotFindUUIDException();
        }
        createPlayer(uuid, name);
        PreparedStatement pst = null;
        try {
            pst = ConnectionPool.getConnection().prepareStatement("update bans set who=" +who + " where id=" + uuid + ";");
            pst.execute();
            pst = ConnectionPool.getConnection().prepareStatement("update bans set time="+time+" where id=" + uuid + ";");
            pst.execute();
            pst = ConnectionPool.getConnection().prepareStatement("update bans set reason="+reason+" where id=" + uuid + ";");
            pst.execute();
            if (LegendWarsBans.getInstance().getProxy().getPlayer(name) != null) {
                ProxiedPlayer player = LegendWarsBans.getInstance().getProxy().getPlayer(name);
                JSONObject obj = JSONUtils.getJSONObject(CONFIG_FILE).getJSONObject("message").getJSONObject("tempban");
                String banReason = (reason == null) ? obj.getString("defaultReason") : reason;
                player.disconnect(new TextComponent(Utils.colorCodes(banReason)));
            }
            JSONObject obj = JSONUtils.getJSONObject(CONFIG_FILE).getJSONObject("message").getJSONObject("tempban");
            JSONObject bc = obj.getJSONObject("broadcast");
            if (bc.getBoolean("enable")) {
                String msg = bc.getString("message");
                msg = msg.replaceAll("%banner%", who);
                msg = msg.replaceAll("%player%", name);
                msg = msg.replaceAll("%reason%", reason);
                msg = msg.replaceAll("%nl%", "\n");
                ProxyServer server = LegendWarsBans.getInstance().getProxy();
                server.broadcast(new TextComponent(Utils.colorCodes(msg)));
            }
            return;
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
            pst = ConnectionPool.getConnection().prepareStatement("update bans set time=-1 where id=" + uuid + ";");
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

    public static void createPlayer(String id, String name) {
        PreparedStatement pst = null;
        try {
            pst = ConnectionPool.getConnection().prepareStatement("select * from bans where id = " + id + ";");
            pst.executeUpdate();
            ResultSet rs = pst.getResultSet();
            if (!rs.next()) {
                pst = ConnectionPool.getConnection().prepareStatement("INSERT INTO `bans` (`id`, `name`, `who`, `time`, `reason`, `unbanreason`) VALUES ('"+id+"', '"+name+"', 'none', '0', 'none', 'none');");
                pst = ConnectionPool.getConnection().prepareStatement("INSERT INTO `mutes` (`id`, `name`, `who`, `time`, `reason`, `unbanreason`) VALUES ('"+id+"', '"+name+"', 'none', '0', 'none', 'none');");
                pst.execute();
            }
            return;
        } catch (SQLException e) {
            return;
        }
    }

    private static long getBanTime(ProxiedPlayer player) {
        if (isBanned(player)) {
            PreparedStatement pst = null;
            try {
                pst = ConnectionPool.getConnection().prepareStatement("select * from bans where id = " + player.getUniqueId().toString() + ";");
                pst.executeUpdate();
                ResultSet rs = pst.getResultSet();
                if (rs.next()) {
                    return (rs.getLong("time"));
                }
            } catch (SQLException e) {
                return 0L;
            }
        }
        return 0L;
    }

    public static long getMuteTime(ProxiedPlayer player) {
        if (isBanned(player)) {
            PreparedStatement pst = null;
            try {
                pst = ConnectionPool.getConnection().prepareStatement("select * from mutes where id = " + player.getUniqueId().toString() + ";");
                pst.executeUpdate();
                ResultSet rs = pst.getResultSet();
                if (rs.next()) {
                    return (rs.getLong("time"));
                }
            } catch (SQLException e) {
                return 0L;
            }
        }
        return 0L;
    }

    public static boolean isMuted(ProxiedPlayer player) {
        if (Utils.unban(getBanTime(player))) {
            try {
                unmute(player.getName(), "Time out", "Console");
            } catch (CannotFindUUIDException e) {
                System.out.println("Could not find uuid of " + player + "");
            }
            return false;
        }
        PreparedStatement pst = null;
        boolean ban = false;
        try {
            pst = ConnectionPool.getConnection().prepareStatement("select * from mutes where id = " + player.getUniqueId().toString() + ";");
            pst.executeUpdate();
            ResultSet rs = pst.getResultSet();
            if (rs.next()) {
                ban = (rs.getLong("time") >= 0);
            }
        } catch (SQLException e) {
            ban = false;
        }
        return ban;
    }

    public static boolean isBanned(ProxiedPlayer player) {
        if (Utils.unban(getBanTime(player))) {
            try {
                unban(player.getName(), "Time out", "Console");
            } catch (CannotFindUUIDException e) {
                System.out.println("Could not find uuid of " + player + "");
            }
            return false;
        }
        PreparedStatement pst = null;
        boolean ban = false;
        try {
            pst = ConnectionPool.getConnection().prepareStatement("select * from bans where id = " + player.getUniqueId().toString() + ";");
            pst.executeUpdate();
            ResultSet rs = pst.getResultSet();
            if (rs.next()) {
                ban = (rs.getLong("time") >= 0);
            }
        } catch (SQLException e) {
            ban = false;
        }
        return ban;
    }

    public static boolean isGroup(ProxiedPlayer player, String group) {
        PreparedStatement pst = null;
        boolean b = true;
        try {
            pst = ConnectionPool.getConnection().prepareStatement("select * from ranks where child = " + player.getUniqueId().toString() + ";");
            pst.executeUpdate();
            ResultSet rs = pst.getResultSet();
            if (rs.next()) {
                b = (rs.getString("parent").toLowerCase().equals(group.toLowerCase()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }

    public static boolean canBan(ProxiedPlayer player) {
        return ((isGroup(player, "Mod")) || (isGroup(player, "Admin")) || (isGroup(player, "Owner")));
    }

    public static boolean canKick(ProxiedPlayer player) {
        return ((isGroup(player, "Mod")) || (isGroup(player, "Admin")) || (isGroup(player, "Owner")) || (isGroup(player, "Helper")));
    }

    public static void unmute(String name, String o, String console) {
    }
}
