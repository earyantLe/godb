package com.earyant.database.util;

import com.earyant.database.explain.TableField;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class GsonUtils {
    public static String toJson(String str) {
        Gson gson = new Gson();
        return gson.toJson(str);
    }

    public static String toJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }

    public static <T> List<T> fromJsonList(String json, Class<T> t) {
        //创建Gson对象
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        //获取JsonArray对象
        JsonArray jsonElements = jsonParser.parse(json).getAsJsonArray();
        ArrayList<T> beans = new ArrayList<>();
        for (JsonElement bean : jsonElements) {
            //解析
            T bean1 = gson.fromJson(bean, t);
            beans.add(bean1);
        }
        return beans;
    }

    public static void main(String[] args) {
        String s = "[{\"fieldName\":\"`ID`\",\"type\":1,\"length\":255,\"defaultContent\":\"NULL\"}]";
        System.out.println(fromJsonList(s, TableField.class));
    }
}
