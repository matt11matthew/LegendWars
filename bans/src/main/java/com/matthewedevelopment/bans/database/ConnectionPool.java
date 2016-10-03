package com.matthewedevelopment.bans.database;
/* 
 * Copyright © 2016 Matthew E Development - All Rights Reserved
 * You may NOT use, distribute and modify this code.
 */

import com.matthewedevelopment.bans.LegendWarsBans;
import com.matthewedevelopment.bans.utils.JSONUtils;
import org.json.JSONObject;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by Matthew E on 10/3/2016 at 4:58 PM.
 */
public class ConnectionPool {

    private static Connection con;

    public static boolean refresh = false;

    public static Connection getConnection() {
        try {
            if ((refresh) || (con == null) || (con.isClosed())) {
                refresh = false;
                if (con != null) {
                    con.close();
                }
                JSONObject obj = JSONUtils.getJSONObject(new File(LegendWarsBans.getInstance().getDataFolder() + "", "config.json")).getJSONObject("mysql");
                String databaseHost = obj.getString("host");
                int databasePort = obj.getInt("port");
                String databaseName = obj.getString("databaseName");
                String username = obj.getString("username");
                String password = obj.getString("password");
                con = DriverManager.getConnection("jdbc:mysql://" + databaseHost + ":" + databasePort + "/" + databaseName, username, password);
            }
        } catch (Exception ee) {
            ee.printStackTrace();
            System.out.println("Couldn't connect to the database!");
        }
        return con;
    }
}