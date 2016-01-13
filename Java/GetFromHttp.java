package com.zhangxy.weatherforecast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Zhangxy on 12/6/2015.
 */
public class GetFromHttp {

    public JSONObject getJsonData(String path) throws JSONException {
        JSONObject resultJson = null;
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int responsecode = conn.getResponseCode();
            if(conn.getResponseCode() == 200){
                InputStream is = conn.getInputStream();
                BufferedReader isbuff = new BufferedReader(new InputStreamReader(is));
                StringBuilder entityStringBuilder = new StringBuilder();
                String line = null;
                while ((line = isbuff.readLine()) != null) {
                    entityStringBuilder.append(line + "/n");
                }
                resultJson = new JSONObject(entityStringBuilder.toString());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultJson;
    }
}