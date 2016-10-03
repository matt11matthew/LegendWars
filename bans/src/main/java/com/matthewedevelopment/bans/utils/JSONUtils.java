package com.matthewedevelopment.bans.utils;
/* 
 * Copyright © 2016 Matthew E Development - All Rights Reserved
 * You may NOT use, distribute and modify this code.
 */


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Matthew E on 10/3/2016 at 4:59 PM.
 */
public class JSONUtils {

    public static JSONObject getJSONObject(File file) {
        try {
            return new JSONObject(getFileText(file));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFileText(File file) {
        String text = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            text = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return text;
    }
}
