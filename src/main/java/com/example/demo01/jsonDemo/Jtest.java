package com.example.demo01.jsonDemo;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Jtest {

    public static void main(String[] args) {
        Map<String,String> map = new HashMap<String,String>(10);
        map.put("key1","value1");
        map.put("key2","value2");
        Gson json = new Gson();
        String result = json.toJson(map);
        System.out.println(result);
        HashMap hashMap = json.fromJson(result, HashMap.class);
        Iterator iterator = hashMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            System.out.println("key:"+entry.getKey()+",value:"+entry.getValue());
        }

    }
}
