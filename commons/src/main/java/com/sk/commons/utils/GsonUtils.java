package com.sk.commons.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;



public class GsonUtils {

    public static <T> T json2bean(String json, Class<? extends T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }

    public static String bean2json(Object object) {
        if (object == null)
            return "";
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static HashMap<String, String> parseMap(String data) {
        GsonBuilder gb = new GsonBuilder();
        Gson g = gb.create();
        return g.fromJson(data, new TypeToken<HashMap<String, String>>() {
        }.getType());
    }

    public static <T> ArrayList<? extends T> parseList(String data) {
        GsonBuilder gb = new GsonBuilder();
        Gson g = gb.create();
        return g.fromJson(data, new TypeToken<ArrayList<? extends T>>() {
        }.getType());
    }

}
