/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rps.client.util;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 *
 * @author kumar-sand
 */
public class RESTUtil {
    
    
    public static Object get(String urlString, Map<String, String> urlParams, Map<String, String> requestProperties, Class cls1, Class cls2) throws Exception {
        StringBuilder sb = new StringBuilder(urlString);
        if(null != urlParams) {
            if(!urlString.contains("?")) {
                sb.append("?");
            }
            for(String key:urlParams.keySet()) {
                sb.append("&");
                sb.append(key);
                sb.append("=");
                sb.append(urlParams.get(key));
            }
        }
        URL url = new URL(urlString);
        HttpURLConnection uc = (HttpURLConnection)url.openConnection();
        if(null != requestProperties) {
            for(String key:requestProperties.keySet()) {
                uc.setRequestProperty(key, requestProperties.get(key));
            }
        }
        uc.setRequestMethod("GET");
        uc.setUseCaches(false);
        uc.setAllowUserInteraction(false);
        uc.setConnectTimeout(60000);
        uc.setReadTimeout(60000);
        uc.connect();
        int status = uc.getResponseCode();
        StringBuilder rsb = new StringBuilder();
        switch(status) {
            case 200:
            case 201:
                BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
                String line;
                while(null != (line = br.readLine())) {
                    rsb.append(line);
                }
                br.close();
        }
        uc.disconnect();
        
        if(null != cls2) {
            return new JSONDeserializer().use("values", cls1).deserialize(rsb.toString(), cls2);
        }
        else {
            return new JSONDeserializer().deserialize(rsb.toString(), cls1);
        }
    }

    public static Object post(String urlString, Map<String, String> urlParams, Map<String, String> requestProperties, Object ob, Class cls) throws Exception {
        StringBuilder sb = new StringBuilder(urlString);
        if(null != urlParams) {
            if(!urlString.contains("?")) {
                sb.append("?");
            }
            for(String key:urlParams.keySet()) {
                sb.append("&");
                sb.append(key);
                sb.append("=");
                sb.append(urlParams.get(key));
            }
        }
        URL url = new URL(urlString);
        HttpURLConnection uc = (HttpURLConnection)url.openConnection();
        if(null != requestProperties) {
            for(String key:requestProperties.keySet()) {
                uc.setRequestProperty(key, requestProperties.get(key));
            }
        }
        uc.setRequestMethod("POST");
        uc.setUseCaches(false);
        uc.setAllowUserInteraction(false);
        uc.setConnectTimeout(60000);
        uc.setReadTimeout(60000);
        uc.setDoInput(true);
        uc.setDoOutput(true);
        
        uc.getOutputStream().write(new JSONSerializer().deepSerialize(ob).getBytes());
        
        uc.connect();
        int status = uc.getResponseCode();
        StringBuilder rsb = new StringBuilder();
        switch(status) {
            case 200:
            case 201:
                BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
                String line;
                while(null != (line = br.readLine())) {
                    rsb.append(line);
                }
                br.close();
        }
        uc.disconnect();
        
        return new JSONDeserializer().deserialize(rsb.toString(), cls);
    }


}
